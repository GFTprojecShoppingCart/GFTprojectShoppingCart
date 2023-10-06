package com.gftproject.shoppingcart;

import java.util.List;
import java.util.Optional;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;

public class CartsData {



    public static Optional<Cart> createCart001() {
        return Optional.of(new Cart(1L, Status.DRAFT));
    }

    public static Optional<Cart> createCart002() {
        return Optional.of(new Cart(2L, Status.DRAFT));
    }

    public static Optional<Cart> createCart003() {
        return Optional.of(new Cart(3L, Status.SUBMITTED));
    }

    public static List<Cart> getMockCarts() {
        Cart mock01 = new Cart(1L, Status.DRAFT);
        Cart mock02 = new Cart(2L, Status.DRAFT);
        Cart mock03 = new Cart(3L, Status.DRAFT);
        return List.of(mock01, mock02, mock03);
    }
}
