package com.gftproject.shoppingcart;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class CartsData {


    public static Cart createCart001() {
        return new Cart(1L, new HashMap<>(), 1L, Status.DRAFT,  BigDecimal.ZERO,  BigDecimal.ZERO);
    }

    public static Cart createSampleCart() {
        return new Cart(1L, new HashMap<>(), 1L, Status.SUBMITTED, new BigDecimal("15"), BigDecimal.ZERO);
    }

    public static Cart createCart002() {
        return new Cart(2L, new HashMap<>(), 1L, Status.DRAFT, new BigDecimal("13"),  BigDecimal.ZERO);
    }

    public static Cart createCart003() {
        return new Cart(3L, new HashMap<>(), 1L, Status.SUBMITTED, new BigDecimal("80"),  BigDecimal.ZERO);
    }

    public static List<Cart> getMockCarts() {
        Cart mock01 = new Cart(1L, new HashMap<>(), 1L, Status.DRAFT, BigDecimal.ZERO,  BigDecimal.ZERO);
        Cart mock02 = new Cart(2L, new HashMap<>(), 1L, Status.DRAFT, new BigDecimal("24"),  BigDecimal.ZERO);
        Cart mock03 = new Cart(3L, new HashMap<>(), 1L, Status.DRAFT, new BigDecimal("23"),  BigDecimal.ZERO);
        Cart mock04 = new Cart(4L, new HashMap<>(), 1L, Status.SUBMITTED, new BigDecimal("25"),  BigDecimal.ZERO);
        return List.of(mock01, mock02, mock03);
    }
}
