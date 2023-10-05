package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;

import java.util.List;

public interface ShoppingCartService {

    List<Cart> getCartsByStatus(Status status);

}