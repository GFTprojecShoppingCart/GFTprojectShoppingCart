package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.model.*;
import com.gftproject.shoppingcart.repositories.*;
import jakarta.transaction.Transactional;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final ShoppingCartRepository cartRepository;
    private final CartComputationsService computationsService;
    private final ProductServiceImpl productService;
    private final UserServiceImpl userService;
    private final CountryRepository countryRepository;
    private final PaymentRepository paymentRepository;
    private final CartProductServiceImpl cartProductService;
    private final CartProductsRepository cartProductRepository;
    private final ProductRepository productRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository cartRepository, CartComputationsService computationsService, ProductServiceImpl productService, UserServiceImpl userService, CountryRepository countryRepository, PaymentRepository paymentRepository, CartProductServiceImpl cartProductService, CartProductsRepository cartProductRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.computationsService = computationsService;
        this.productService = productService;
        this.userService = userService;
        this.countryRepository = countryRepository;
        this.paymentRepository = paymentRepository;
        this.cartProductService = cartProductService;
        this.cartProductRepository = cartProductRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    @Override
    public List<Cart> findAllByUserId(Long userId) {
        return cartRepository.findAllByUserId(userId);
    }

    @Override
    public List<Cart> findAllByStatus(Status status) {
        return cartRepository.findAllByStatus(status);
    }

    @Override
    public Cart createCart(Long userId) {
        Cart cart = new Cart();

        cart.setUserId(userId);
        cart.setFinalPrice(new BigDecimal(0));
        cart.setFinalWeight(new BigDecimal(0));
        cart.setStatus(Status.DRAFT);

        return cartRepository.save(cart);
    }

    public Cart addProductToCartWithQuantityOof(Long userId, Long cartId, Long productId, int quantity) throws NotEnoughStockException {

        Optional<Cart> optionalCart = cartRepository.findById(cartId);

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            List<CartProduct> products = new ArrayList<>();
//            ProductDTO request = productService.getProductById(productId);
            CartProduct existingProduct = null;
            ProductDTO request = new ProductDTO(1, new BigDecimal(3), 5, new BigDecimal(6));

            if (quantity > request.getStock()) {
                throw new NotEnoughStockException(List.of(productId));
            }

            boolean productExists = false;
            for (CartProduct product : products) {
                if (product.getProduct().getId() == productId) {
                    product.setQuantity(quantity);
                    existingProduct = product;
                    productExists = true;
                    break;
                }
            }

            if (!productExists) {
                CartProduct newProduct = new CartProduct();
                products.add(newProduct);
            }

//            cart.setCartProducts(products);

            return cartRepository.save(cart);
        } else {
            Cart newCart = new Cart(1, userId, Status.DRAFT, BigDecimal.valueOf(0), BigDecimal.valueOf(0));
            return cartRepository.save(newCart);
        }
    }

    @Autowired
    public Cart addProductToCartWithQuantity(Long userId, Long cartId, Long productId, int quantity) throws ProductNotFoundException {
        // Check if the cart exists or create a new one if it doesn't
        Cart cart = cartRepository.findById(cartId).orElseGet(() -> {
            Cart newCart = new Cart(userId, Status.DRAFT, BigDecimal.ZERO, BigDecimal.ZERO);
            return cartRepository.save(newCart);
        });

        // Check if the product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        // Check if the product is already in the cart
        CartProduct cartProduct = cartProductRepository.findByCartAndProduct(cart, product);
        if (cartProduct != null) {
            // Product is in the cart, update the quantity
            cartProduct.setQuantity(cartProduct.getQuantity() + quantity);
        } else {
            // Product is not in the cart, add it
            cartProduct = new CartProduct(cart, product, quantity);
        }

        // Save the changes to the cart and cartProduct
        cartRepository.save(cart);
        cartProductRepository.save(cartProduct);

        return cart;
    }

/*

    public Cart addProductToCartWithQuantityOof2(Long userId, Long cartId, Long productId, int quantity) throws ProductNotFoundException, NotEnoughStockException {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            List<CartProduct> products = cartProductRepository.findAllByCartId(cartId);
            CartProduct existingProduct = null;

            // Check if the product exists in the cart
            for (CartProduct product : products) {
                if (product.getProduct().getId() == productId) {
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
                    CartProduct newProduct = new Product();
                    newProduct.getProduct().setId(productId);
                    newProduct.setQuantity(quantity);
//                    newProduct.setStorageQuantity(quantity); // Adjust as needed
                    productRepository.save(newProduct); // Persist the new product
                    existingProduct = newProduct;
                }

                products.add(existingProduct);
            }

            // Update the quantity
            existingProduct.setQuantity(quantity);

            // Check stock and handle exceptions as needed

//            cart.setCartProducts(products);
            return cartRepository.save(cart);
        } else {
            Cart newCart = new Cart(userId, Status.DRAFT, BigDecimal.valueOf(0), BigDecimal.valueOf(0));
            return cartRepository.save(newCart);
        }
    }

*/

    @Override
    public Cart submitCart(Long idCart) throws NotEnoughStockException, ProductNotFoundException {

        // Obtenemos el carrito
        Cart cart = cartRepository.findById(idCart).orElseThrow();

        User user = userService.getUserById(cart.getUserId());
        Optional<Country> country = countryRepository.findById(user.getCountry());
        Optional<Payment> payment = paymentRepository.findById(user.getPaymentMethod());

        if (country.isEmpty() || payment.isEmpty()) {
            throw new ProductNotFoundException("User data incomplete");
        }

        // Primero vemos si es valido desde la ultima revision
        List<Long> productsNoStock = computationsService.getProductIdsWithoutStock(null);
        if (!productsNoStock.isEmpty()) {
            throw new NotEnoughStockException(productsNoStock);
        }

        // Obtain IDs of the products in cart
        List<CartProduct> productList = new ArrayList<>();

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
            return cartRepository.save(cart);
        }

        return cart;


    }


    public void updateProductsFromCarts(List<ProductDTO> updatedProducts) {

        List<Long> productsIds = updatedProducts.stream().map(ProductDTO::getId).toList();
//        List<Cart> shoppingCarts = shoppingCartRepository.findCartsByProductIds(productsIds);
        List<Cart> shoppingCarts = new ArrayList<>();

        // Create a map for faster access
        Map<Long, ProductDTO> productMap = updatedProducts.stream()
                .collect(Collectors.toMap(ProductDTO::getId, Function.identity()));

        for (Cart cart : shoppingCarts) {
            List<CartProduct> cartProducts = null;
            for (CartProduct product : cartProducts) {
                //If product exists on the map of updated products
                ProductDTO updatedDTO = productMap.get(product.getProduct().getId());
                if (updatedDTO != null) {
                    product.getProduct().setStorageQuantity(updatedDTO.getStock());
                }
            }
            cartRepository.save(cart);
        }

//        return shoppingCarts;
    }

    @Override
    @Transactional
    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
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
