package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.CartsData;
import com.gftproject.shoppingcart.ProductData;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.model.Cart;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartComputationsServiceTest {

    private Cart cart;
    private CartComputationsService computationsService;

    @BeforeEach
    void setUp() {
        computationsService = new CartComputationsService();
        cart = CartsData.createCart001();
        cart.setProducts(ProductData.getMockProductMap());
    }

    @Test
    @DisplayName("GIVEN a shopping cart and a Product list WHEN it scans if there's enough stock for the products in cart THEN will return the elements without enough stock")
    void checkStock() throws ProductNotFoundException {
        //TODO
        assertThat(computationsService.checkStock(cart.getProducts(), ProductData.getWarehouseStock())).contains(5L);
    }

    @Test
    @DisplayName("GIVEN a shopping cart and a Product list WHEN method is called THEN returns the final price and weight of all products in cart ")
    void computeFinalValues() {

        Pair<BigDecimal, BigDecimal> pairWeightValue = computationsService.computeFinalValues(cart.getProducts(), ProductData.getWarehouseStock());

        assertThat(new BigDecimal("120.4")).isEqualTo(pairWeightValue.a);
        assertThat(new BigDecimal("287.86")).isEqualTo(pairWeightValue.b);
    }
}