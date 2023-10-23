package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.model.*;
import com.gftproject.shoppingcart.repositories.CountryRepository;
import com.gftproject.shoppingcart.repositories.PaymentRepository;
import com.gftproject.shoppingcart.repositories.ProductRepository;
import com.gftproject.shoppingcart.repositories.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartComputationsService computationsService;
    private final ProductServiceImpl productService;
    private final UserServiceImpl userService;
    private final CountryRepository countryRepository;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, CartComputationsService computationsService, ProductServiceImpl productService, UserServiceImpl userService, CountryRepository countryRepository, PaymentRepository paymentRepository, ProductRepository productRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.computationsService = computationsService;
        this.productService = productService;
        this.userService = userService;
        this.countryRepository = countryRepository;
        this.paymentRepository = paymentRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<Cart> findAll() {
        return shoppingCartRepository.findAll();
    }

    @Override
    public List<Cart> findAllByUserId(Long userId) {
        return shoppingCartRepository.findAllByUserId(userId);
    }

    @Override
    public List<Cart> findAllByStatus(Status status) {
        return shoppingCartRepository.findAllByStatus(status);
    }

    @Override
    public Cart createCart(Long userId) {
        Cart cart = new Cart();

        cart.setUserId(userId);
        cart.setFinalPrice(new BigDecimal(0));
        cart.setFinalWeight(new BigDecimal(0));
        cart.setProductList(new ArrayList<>());
        cart.setStatus(Status.DRAFT);

        return shoppingCartRepository.save(cart);
    }

    public Cart addProductToCartWithQuantityOof(Long userId, Long cartId, Long productId, int quantity) throws ProductNotFoundException, NotEnoughStockException {

        Optional<Cart> optionalCart = shoppingCartRepository.findById(cartId);

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            List<Product> products = cart.getProductList();
//            ProductDTO request = productService.getProductById(productId);
            Product existingProduct = null;
            ProductDTO request = new ProductDTO(1, new BigDecimal(3), 5, new BigDecimal(6));

            if (quantity > request.getStock()) {
                throw new NotEnoughStockException(List.of(productId));
            }

            boolean productExists = false;
            for (Product product : products) {
                if (product.getId() == productId) {
                    product.setQuantity(quantity);
                    existingProduct = product;
                    productExists = true;
                    break;
                }
            }

            if (!productExists) {
                Product newProduct = new Product(productId, quantity, request.getStock());
                products.add(newProduct);
            }

            cart.setProductList(products);

            return shoppingCartRepository.save(cart);
        } else {
            Cart newCart = new Cart(new ArrayList<>(), userId, Status.DRAFT, BigDecimal.valueOf(0), BigDecimal.valueOf(0));
            return shoppingCartRepository.save(newCart);
        }
    }

    @Override
    public Cart addProductToCartWithQuantity(Long userId, Long cartId, Long productId, int quantity) throws ProductNotFoundException, NotEnoughStockException {
        Optional<Cart> optionalCart = shoppingCartRepository.findById(cartId);

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            List<Product> products = cart.getProductList();
            Product existingProduct = null;

            // Check if the product exists in the cart
            for (Product product : products) {
                if (product.getId() == productId) {
                    existingProduct = product;
                    break;
                }
            }

            if (existingProduct == null) {
                // Product doesn't exist in the cart, let's check if it exists in the database
                Optional<Product> productOptional = productRepository.findById(productId);

                if (productOptional.isPresent()) {
                    existingProduct = productOptional.get();
                } else {
                    // Product doesn't exist in the database, so create a new one
                    Product newProduct = new Product();
                    newProduct.setId(productId);
                    newProduct.setQuantity(quantity);
                    newProduct.setStorageQuantity(quantity); // Adjust as needed
                    productRepository.save(newProduct); // Persist the new product
                    existingProduct = newProduct;
                }

                products.add(existingProduct);
            }

            // Update the quantity
            existingProduct.setQuantity(quantity);

            // Check stock and handle exceptions as needed

            cart.setProductList(products);
            return shoppingCartRepository.save(cart);
        } else {
            Cart newCart = new Cart(new ArrayList<>(), userId, Status.DRAFT, BigDecimal.valueOf(0), BigDecimal.valueOf(0));
            return shoppingCartRepository.save(newCart);
        }
    }


    @Override
    public Cart submitCart(Long idCart) throws NotEnoughStockException, ProductNotFoundException {

        // Obtenemos el carrito
        Cart cart = shoppingCartRepository.findById(idCart).orElseThrow();

        User user = userService.getUserById(cart.getUserId());
        Optional<Country> country = countryRepository.findById(user.getCountry());
        Optional<Payment> payment = paymentRepository.findById(user.getPaymentMethod());

        if (country.isEmpty() || payment.isEmpty()) {
            throw new ProductNotFoundException("User data incomplete");
        }

        // Primero vemos si es valido desde la ultima revision
        List<Long> productsNoStock = computationsService.getProductIdsWithoutStock(cart.getProductList());
        if (!productsNoStock.isEmpty()) {
            throw new NotEnoughStockException(productsNoStock);
        }

        // Obtain IDs of the products in cart
        List<Product> productList = cart.getProductList();

        // Comunicar al almacen la compra
        List<ProductDTO> submittedProducts = productService.submitPurchase(productList);

        // Submitted products  si la compra se ha relalizado correctamente
        if (!submittedProducts.isEmpty()) {
            // Calculate price
            Pair<BigDecimal, BigDecimal> pair;
            pair = computationsService.computeFinalValues(productList, submittedProducts);

            // Change cart status
            cart.setFinalWeight(pair.a);
            cart.setFinalPrice(applyTaxes(pair.b, pair.a, user));
            cart.setStatus(Status.SUBMITTED);

            // Update the cart
            return shoppingCartRepository.save(cart);
        }

        return cart;


    }


    public List<Cart> updateProductsFromCarts(List<ProductDTO> updatedProducts) {

        List<Long> productsIds = updatedProducts.stream().map(ProductDTO::getId).toList();
        List<Cart> shoppingCarts = shoppingCartRepository.findCartsByProductIds(productsIds);

        // Create a map for faster access
        Map<Long, ProductDTO> productMap = updatedProducts.stream()
                .collect(Collectors.toMap(ProductDTO::getId, Function.identity()));

        for (Cart cart : shoppingCarts) {
            List<Product> cartProducts = cart.getProductList();
            for (Product product : cartProducts){
                //If product exists on the map of updated products
                ProductDTO updatedDTO = productMap.get(product.getId());
                if (updatedDTO != null){
                    product.setStorageQuantity(updatedDTO.getStock());
                }
            }
            shoppingCartRepository.save(cart);
        }

        return shoppingCarts;
    }

    @Override
    @Transactional
    public void deleteCart(Long cartId) {
        shoppingCartRepository.deleteById(cartId);
    }

    public BigDecimal applyTaxes(BigDecimal originalPrice, BigDecimal weight, User user) {

        BigDecimal priceWithTaxes = new BigDecimal(0);
        priceWithTaxes = priceWithTaxes.add(originalPrice);

        double weightPercentage = computationsService.computeByWeight(weight.doubleValue());
        double cardPercentage = paymentRepository.findById(user.getPaymentMethod()).orElseThrow().getChargePercentage();
        double countryPercentage = countryRepository.findById(user.getCountry()).orElseThrow().getTaxPercentage();

        BigDecimal finalWeightPrice = priceWithTaxes.multiply(BigDecimal.valueOf(weightPercentage));
        BigDecimal finalCardPrice = priceWithTaxes.multiply(BigDecimal.valueOf(cardPercentage));
        BigDecimal finalCountryPrice = priceWithTaxes.multiply(BigDecimal.valueOf(countryPercentage));

        priceWithTaxes = priceWithTaxes.add(finalCardPrice);
        priceWithTaxes = priceWithTaxes.add(finalWeightPrice);
        priceWithTaxes = priceWithTaxes.add(finalCountryPrice);

        return priceWithTaxes;
    }

}
