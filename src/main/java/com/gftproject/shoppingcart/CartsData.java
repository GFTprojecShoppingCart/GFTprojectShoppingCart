package com.gftproject.shoppingcart;

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
}
