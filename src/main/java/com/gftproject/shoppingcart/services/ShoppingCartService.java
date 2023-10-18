package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Product;
import com.gftproject.shoppingcart.model.Status;

import java.util.List;

public interface ShoppingCartService {

    List<Cart> findAllByStatus(Status status);

    List<Cart> findAll();

    Cart createCart(Long userId);

    Cart addProductToCartWithQuantity(Long cartId, Long productId, int quantity) throws ProductNotFoundException;

    void deleteCart(Long idCart);

    Cart submitCart(Long idCart) throws NotEnoughStockException, ProductNotFoundException;

    List<Cart> updateProductsFromCarts(List<Product> products);
}