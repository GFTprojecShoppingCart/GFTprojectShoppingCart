package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.ProductData;
import com.gftproject.shoppingcart.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CartComputationsServiceTest {

    @BeforeEach
    void setUp() {
        Product products = ProductData.createProduct001();
    }

    @Test
    void computePrice() {
    }

    @Test
    void checkStock() {
    }

    @Test
    void computeFinalValues() {


        assertThat(2).isEqualTo(2);
    }
}