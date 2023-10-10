package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Product;
import com.gftproject.shoppingcart.model.Status;

import java.util.List;

public interface ShoppingCartService {

    List<Cart> findAllByStatus(Status status);

    List<Cart> findAll();

    Cart createCart(Long id_user);

    Cart addProductToCartWithQuantity(Long cartId, Product product, int quantity);
}