package com.gftproject.shoppingcart.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartIsAlreadySubmittedExceptionTest {
    @Test
    @DisplayName("GIVEN a list of product IDs WHEN creating NotEnoughStockException THEN the message should contain the product IDs")
    void testCartIsAlreadySubmittedException() {

        CartIsAlreadySubmittedException exception = new CartIsAlreadySubmittedException(1L);

        assertThat(exception).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("The selected cart is already closed and cannot be modified: 1");

    }
}
