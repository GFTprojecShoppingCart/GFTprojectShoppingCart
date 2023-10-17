package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.CartsData;
import com.gftproject.shoppingcart.ProductData;
import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

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
    void checkStock() {
        assertThat(computationsService.checkStock(cart.getProducts(), ProductData.getWarehouseStock())).contains(5L);
    }

    @Test
    @DisplayName("Checks if enough stock")
    void throwsCheckStock() {
        cart.setProducts(ProductData.getLowWarehouseStock());
        assertThat(computationsService.checkStock(cart.getProducts(), ProductData.getWarehouseStock())).isEmpty();
    }

    @Test
    @DisplayName("Compute final values")
    void computeFinalValues() throws NotEnoughStockException {

        computationsService.computeFinalValues(cart.getProducts(), new ArrayList<>());

        assertThat(new BigDecimal("120.44")).isEqualTo(cart.getFinalPrice());
        assertThat(new BigDecimal("114.1")).isEqualTo(cart.getFinalWeight());
    }

    @Test
    @DisplayName("Assert exception in compute values")
    void throwsFinalValues() {

        cart.setProducts(ProductData.getLowWarehouseStock());

        assertThatThrownBy(() -> {
            computationsService.computeFinalValues(cart.getProducts(), ProductData.getWarehouseStock());
        }).isInstanceOf(NotEnoughStockException.class);


        assertThat(new BigDecimal(0)).isEqualTo(cart.getFinalPrice());
        assertThat(new BigDecimal(0)).isEqualTo(cart.getFinalWeight());
    }
}