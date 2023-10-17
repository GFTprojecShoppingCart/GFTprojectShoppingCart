package com.gftproject.shoppingcart;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartsData {


    public static Cart createCart001() {
        return new Cart(1L, new ArrayList<>(), new HashMap<>(), 1L, Status.DRAFT,  BigDecimal.ZERO,  BigDecimal.ZERO);
    }

    public static Cart createCart002() {
        return new Cart(2L, new ArrayList<>(), new HashMap<>(), 1L, Status.DRAFT, new BigDecimal("13"),  BigDecimal.ZERO);
    }

    public static Cart createCart003() {
        return new Cart(3L, new ArrayList<>(),new HashMap<>(), 1L, Status.DRAFT, new BigDecimal("80"),  BigDecimal.ZERO);
    }

    public static Cart createCart004() {
        return new Cart(4L, new ArrayList<>(),new HashMap<>(), 1L, Status.SUBMITTED, new BigDecimal("80"),  BigDecimal.ZERO);
    }

    public static List<Cart> getMockCarts() {
        Cart mock01 = createCart001();
        Cart mock02 = createCart002();
        Cart mock03 = createCart003();
        Cart mock04 = createCart004();
        return List.of(mock01, mock02, mock03, mock04);
    }
}
