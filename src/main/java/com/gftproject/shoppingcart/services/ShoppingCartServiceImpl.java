package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.CartIsAlreadySubmittedException;
import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.exceptions.UserNotFoundException;
import com.gftproject.shoppingcart.model.*;
import com.gftproject.shoppingcart.repositories.CartProductsRepository;
import com.gftproject.shoppingcart.repositories.CartRepository;
import com.gftproject.shoppingcart.repositories.CountryRepository;
import com.gftproject.shoppingcart.repositories.PaymentRepository;
import jakarta.transaction.Transactional;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final CartRepository cartRepository;
    private final CartComputationsService computationsService;
    private final ProductServiceImpl productService;
    private final UserService userService;
    private final CountryRepository countryRepository;
    private final PaymentRepository paymentRepository;
    private final CartProductsRepository cartProductRepository;

    public ShoppingCartServiceImpl(CartRepository cartRepository, CartComputationsService computationsService, ProductServiceImpl productService, UserService userService, CountryRepository countryRepository, PaymentRepository paymentRepository, CartProductsRepository cartProductRepository) {
        this.cartRepository = cartRepository;
        this.computationsService = computationsService;
        this.productService = productService;
        this.userService = userService;
        this.countryRepository = countryRepository;
        this.paymentRepository = paymentRepository;
        this.cartProductRepository = cartProductRepository;
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
    public Cart createCart(Long userId) throws UserNotFoundException {
        Cart cart = new Cart();

        cart.setUserId(userId);
        cart.setFinalPrice(new BigDecimal(0));
        cart.setFinalWeight(new BigDecimal(0));
        cart.setStatus(Status.DRAFT);

        return cartRepository.save(cart);
    }

    @Override
    public Cart addProductToCartWithQuantity(long userId, long cartId, long productId, int quantity) throws ProductNotFoundException, NotEnoughStockException, CartIsAlreadySubmittedException {
        // Check if the cart exists or create a new one if it doesn't
        Cart cart = cartRepository.findById(cartId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setFinalPrice(new BigDecimal(0));
            newCart.setFinalWeight(new BigDecimal(0));
            newCart.setStatus(Status.DRAFT);

            return cartRepository.save(newCart);
        });

        if (cart.getStatus().equals(Status.SUBMITTED)){
            throw new CartIsAlreadySubmittedException(cartId);
        }

        // Check if the product exists
        ProductDTO product = productService.getProductById(productId);
        if (quantity <= product.getStock()) {

                // Check if the product is already in the cart
                CartProduct cartProduct = cartProductRepository.findByCartAndProduct(cart, productId);

            if (cartProduct != null) {
                // Product is in the cart, update the quantity
                cartProduct.setQuantity(quantity);
            } else {
                // Product is not in the cart, add it
                cartProduct = new CartProduct(cart, productId, true, quantity);
            }

            cart.setFinalPrice(cart.getFinalPrice().add(product.getPrice()));
            cart.setFinalWeight(cart.getFinalWeight().add(product.getWeight()));
            // Save the changes to the cart and cartProduct
            cartRepository.save(cart);
            cartProductRepository.save(cartProduct);

        } else {
            throw new NotEnoughStockException(List.of(productId));
        }
        return cart;
    }

    @Override
    public Cart submitCart(Long idCart) throws NotEnoughStockException, ProductNotFoundException, UserNotFoundException, CartIsAlreadySubmittedException {

        // Obtain the cart
        Cart cart = cartRepository.findById(idCart).orElseThrow();

        if (cart.getStatus().equals(Status.SUBMITTED)){
            throw new CartIsAlreadySubmittedException(idCart);
        }

        List<CartProduct> cartProductList = cartProductRepository.findAllByCartId(idCart);

        User user = userService.getUserById(cart.getUserId());
        Optional<Country> country = countryRepository.findById(user.getCountry());
        Optional<Payment> payment = paymentRepository.findById(user.getPaymentMethod());

        if (country.isEmpty() || payment.isEmpty()) {
            throw new UserNotFoundException("User data incomplete");
        }

        // Check that all elements of the cart have stock = true
        List<Long> invalidProductIds = cartProductList.stream()
                .filter(cartProduct -> !cartProduct.isValid())
                .map(CartProduct::getProduct).toList();

        if (!invalidProductIds.isEmpty()) {
            throw new NotEnoughStockException(invalidProductIds);
        }

        // Communicate the purchase to the warehouse service
        List<ProductDTO> submittedProducts = productService.submitPurchase(cartProductList);

        Pair<BigDecimal, BigDecimal> pair = computationsService.computeFinalWeightAndPrice(cartProductList, submittedProducts);
        double cardPercentage = paymentRepository.findById(user.getPaymentMethod()).orElseThrow().getChargePercentage();
        double countryPercentage = countryRepository.findById(user.getCountry()).orElseThrow().getTaxPercentage();

        double weightTax = computationsService.computeTaxByWeight(pair.a.doubleValue());

        // Change cart status and final values if the purchase was made correctly
        cart.setFinalWeight(pair.a);
        cart.setFinalPrice(computationsService.applyTaxes(pair.b, weightTax, cardPercentage, countryPercentage));
        cart.setStatus(Status.SUBMITTED);

        // Update the cart into the database
        return cartRepository.save(cart);
    }


    @Override
    public void updateProductsFromCarts(List<ProductDTO> updatedProducts) {

        // Transform the list into a map of Ids
        Map<Long, ProductDTO> productMap = updatedProducts.stream()
                .collect(Collectors.toMap(ProductDTO::getId, dto -> dto));

        // We obtain a list of Ids of the updated products
        List<Long> productsIds = updatedProducts.stream().map(ProductDTO::getId).toList();

        // Receive all CartProducts affected by the update
        List<CartProduct> cartProductList = cartProductRepository.findByProductIn(productsIds);

        for (CartProduct product : cartProductList) {
            ProductDTO productDTO = productMap.get(product.getProduct());
            product.setValid(product.getQuantity() <= productDTO.getStock());
            cartProductRepository.save(product);
        }

    }

    @Override
    public void deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow();
        CartProduct product = cartProductRepository.findByCartAndProduct(cart, productId);
        cartProductRepository.delete(product);
    }

    @Override
    @Transactional
    public void deleteCart(Long cartId) {
        for (CartProduct product : cartProductRepository.findAllByCartId(cartId)) {
            cartProductRepository.delete(product);
        }
        cartRepository.deleteById(cartId);
    }

}
