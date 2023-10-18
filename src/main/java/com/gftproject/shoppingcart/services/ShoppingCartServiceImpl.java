package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Product;
import com.gftproject.shoppingcart.model.Status;
import com.gftproject.shoppingcart.repositories.ShoppingCartRepository;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartComputationsService computationsService;
    private final ProductServiceImpl productService;
    private final UserServiceImpl userService;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, CartComputationsService computationsService, ProductServiceImpl productService, UserServiceImpl userService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.computationsService = computationsService;
        this.productService = productService;
        this.userService = userService;
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
    public Cart addProductToCartWithQuantity(Long cartId, Long productId, int quantity) {
        Optional<Cart> optionalCart = shoppingCartRepository.findById(cartId);

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();

            Product product = productService.getProductById(productId);

            addProductWithQuantity(cart, product, quantity);
            return shoppingCartRepository.save(cart);
        } else {
            Cart newCart = new Cart();
            newCart.setStatus(Status.DRAFT);
            return shoppingCartRepository.save(newCart);
        }
    }


    @Override
    public Cart submitCart(Long idCart) {

        try {
            // We obtain the cart
            Cart cart = shoppingCartRepository.findById(idCart).orElseThrow();
            List<Long> productIds = new ArrayList<>(cart.getProducts().keySet());

            // We obtain the cart products from their endpoint
            List<Product> warehouseStock = productService.getProductsByIds(productIds);
            List<Long> productsWithoutStock = null;
            productsWithoutStock = computationsService.checkStock(cart.getProducts(), warehouseStock);

            // TODO Validate User

            userService.validate();

            // TODO Compute price -> Cosas

            if (productsWithoutStock.isEmpty()) {

                // TODO Check TAX / Payment / weight

                // TODO Check cart validity
                // TODO Change status AND send to almacen

                Pair<BigDecimal, BigDecimal> pair = computationsService.computeFinalValues(cart.getProducts(), warehouseStock);

                cart.setFinalWeight(pair.a);
                cart.setFinalPrice(pair.b);
                cart.setStatus(Status.SUBMITTED);

                return shoppingCartRepository.save(cart);
            }
        } catch (ProductNotFoundException e) {
//            throw new RuntimeException(e);
        }
        return new Cart();
    }

    public List<Cart> updateProductsFromCarts(List<Product> productList) {

        List<Long> productsIds = productList.stream().map(Product::getId).toList();
        List<Cart> shoppingCarts = shoppingCartRepository.findCartsByProductIds(productsIds);

        try {

            for (Cart cart : shoppingCarts) {
                cart.setInvalidProducts(computationsService.checkStock(cart.getProducts(), productList));
                shoppingCartRepository.save(cart);
            }

        } catch (ProductNotFoundException e) {
            // TODO
            System.out.println("Oof");
        }
        return shoppingCarts;
    }

    public void addProductWithQuantity(Cart cart, Product product, int quantity) {

        Map<Long, Integer> products = cart.getProducts();
        if (cart.getProducts().containsKey(product)) {
            if (product.getStorageQuantity() >= quantity) {
                int currentQuantity = products.get(product);
                int newQuantity = currentQuantity + quantity;
                products.put(product.getId(), currentQuantity + newQuantity);
            }
        } else {
            products.put(product.getId(), quantity);
        }
    }

    @Override
    public void deleteCart(Long cartId) {
        shoppingCartRepository.deleteById(cartId);
    }

}
