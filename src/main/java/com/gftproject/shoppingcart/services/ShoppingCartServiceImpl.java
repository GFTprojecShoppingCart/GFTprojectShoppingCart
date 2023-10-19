package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.model.*;
import com.gftproject.shoppingcart.repositories.CountryRepository;
import com.gftproject.shoppingcart.repositories.PaymentRepository;
import com.gftproject.shoppingcart.repositories.ShoppingCartRepository;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartComputationsService computationsService;
    private final ProductServiceImpl productService;
    private final UserServiceImpl userService;
    private final CountryRepository countryRepository;
    private final PaymentRepository paymentRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, CartComputationsService computationsService, ProductServiceImpl productService, UserServiceImpl userService, CountryRepository countryRepository, PaymentRepository paymentRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.computationsService = computationsService;
        this.productService = productService;
        this.userService = userService;
        this.countryRepository = countryRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<Cart> findAll() {
        return shoppingCartRepository.findAll();
    }

    @Override
    public List<Cart> findAllByStatus(Status status) {
        return shoppingCartRepository.findAllByStatus(status);
    }

    @Override
    public Cart createCart(Long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        return shoppingCartRepository.save(cart);
    }

    @Override
    public Cart addProductToCartWithQuantity(Long cartId, Long productId, int quantity) throws ProductNotFoundException {
        Optional<Cart> optionalCart = shoppingCartRepository.findById(cartId);

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();

            Product product = productService.getProductById(productId);

            Map<Long, Integer> products = cart.getProducts();

            if (product.getStorageQuantity() >= quantity) {

                products.put(product.getId(), quantity);
            }

            return shoppingCartRepository.save(cart);
        } else {
            Cart newCart = new Cart();
            newCart.setStatus(Status.DRAFT);
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

        if (country.isEmpty() || payment.isEmpty()){
            throw new ProductNotFoundException("User data incomplete");
        }

        // Primero vemos si es valido desde la ultima revision
        if (!cart.getInvalidProducts().isEmpty()) {
            throw new NotEnoughStockException(cart.getInvalidProducts());
        }

        // Obtener IDs de los productos en el carrito
        Map<Long, Integer> productsMap = cart.getProducts();

        // Comunicar al almacen la compra
        List<Product> submittedProducts = productService.getProductsToSubmit(productsMap);

        if (!submittedProducts.isEmpty()) {
            // Calculate price
            Pair<BigDecimal, BigDecimal> pair;
            pair = computationsService.computeFinalValues(cart.getProducts(), submittedProducts);

            double weightPercentage = computationsService.computeByWeight(0);

            // Change cart status
            cart.setFinalWeight(pair.a);
            cart.setFinalPrice(pair.b);
            cart.setStatus(Status.SUBMITTED);

            // Update the cart
            return shoppingCartRepository.save(cart);
        }

        return cart;


    }

    public List<Cart> updateProductsFromCarts(List<Product> productList) {

        List<Long> productsIds = productList.stream().map(Product::getId).toList();
        List<Cart> shoppingCarts = shoppingCartRepository.findCartsByProductIds(productsIds);

        for (Cart cart : shoppingCarts) {
            cart.setInvalidProducts(computationsService.checkStock(cart.getProducts(), productList));
            shoppingCartRepository.save(cart);
        }

        return shoppingCarts;
    }

    @Override
    public void deleteCart(Long cartId) {
        shoppingCartRepository.deleteById(cartId);
    }


}
