package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.ProductData;
import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;
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
        cart = new Cart(1L, ProductData.getMockProductMap(), 1L, Status.DRAFT, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    @Test
    void checkStock() {
        assertThat(computationsService.checkStock(cart.getProducts()));
    }

    @Test
    @DisplayName("Checks if enough stock")
    void throwsCheckStock() {
        cart.setProducts(ProductData.getFaultyMockProductMap());
        assertThat(!computationsService.checkStock(cart.getProducts()));
    }

    @Test
    @DisplayName("Compute final values")
    void computeFinalValues() throws NotEnoughStockException {

        computationsService.computeFinalValues(cart);

        assertThat(new BigDecimal("120.44")).isEqualTo(cart.getFinalPrice());
        assertThat(new BigDecimal("114.1")).isEqualTo(cart.getFinalWeight());
    }

    @Test
    @DisplayName("Assert exception in compute values")
    void throwsFinalValues() {

        cart.setProducts(ProductData.getFaultyMockProductMap());

        assertThatThrownBy(() -> {
            computationsService.computeFinalValues(cart);
        }).isInstanceOf(NotEnoughStockException.class);


        assertThat(new BigDecimal(0)).isEqualTo(cart.getFinalPrice());
        assertThat(new BigDecimal(0)).isEqualTo(cart.getFinalWeight());
    }
}