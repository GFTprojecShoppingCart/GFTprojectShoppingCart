package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.CartsData;
import com.gftproject.shoppingcart.ProductData;
import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.CartProduct;
import com.gftproject.shoppingcart.model.ProductDTO;
import com.gftproject.shoppingcart.model.Status;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CartComputationsServiceTest {

    private CartComputationsService computationsService;
    List<CartProduct> list;

    @BeforeEach
    void setUp() {
        computationsService = new CartComputationsService();
        Cart cart = CartsData.createCart001();
        CartProduct cartProduct01 = new CartProduct(cart, 1L, true, 5);
        CartProduct cartProduct02 = new CartProduct(cart, 2L, true, 2);
        CartProduct cartProduct03 = new CartProduct(cart, 3L, true, 4);

        list = List.of(cartProduct01, cartProduct02, cartProduct03);
    }

   @Test
    @DisplayName("GIVEN a shopping cart and a Product list WHEN method is called THEN returns the final price and weight of all products in cart ")
    void computeFinalValues() {

        Pair<BigDecimal, BigDecimal> pairWeightValue = computationsService.computeFinalWeightAndPrice(list, ProductData.getWarehouseStock());

        assertThat(new BigDecimal("280.2")).isEqualTo(pairWeightValue.a);
        assertThat(new BigDecimal("219.93")).isEqualTo(pairWeightValue.b);
    }
}