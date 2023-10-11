package com.gftproject.shoppingcart;

import java.util.List;
import java.util.Optional;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;

public class CartsData {


    public static Cart createCart001() {
        return new Cart(1L, 1L, Status.DRAFT, 0, 0);
    }

    public static Cart createSampleCart() {
        return new Cart(1L, 1L, Status.SUBMITTED, 15, 0);
    }

    public static Optional<Cart> createCart002() {
        return Optional.of(new Cart(2L, 1L, Status.DRAFT, 13, 0));
    }

    public static Optional<Cart> createCart003() {
        return Optional.of(new Cart(3L, 1L, Status.SUBMITTED, 80, 0));
    }

    public static List<Cart> getMockCarts() {
        Cart mock01 = new Cart(1L, 1L, Status.DRAFT,0, 0);
        Cart mock02 = new Cart(2L, 1L, Status.DRAFT,24, 0);
        Cart mock03 = new Cart(3L, 1L, Status.DRAFT,23, 0);
        Cart mock04 = new Cart(4L, 1L, Status.SUBMITTED, 25, 0);
        return List.of(mock01, mock02, mock03);
    }
}
