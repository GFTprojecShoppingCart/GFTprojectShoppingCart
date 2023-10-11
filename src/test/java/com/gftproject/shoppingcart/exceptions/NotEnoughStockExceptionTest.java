package com.gftproject.shoppingcart.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotEnoughStockExceptionTest {

    @Test
    public void testNoArgConstructor() {
        NotEnoughStockException exception = new NotEnoughStockException();
        assertEquals("Not enough stock available", exception.getMessage());
    }

    @Test
    public void testMessageConstructor() {
        NotEnoughStockException exception = new NotEnoughStockException("Custom message");
        assertEquals("Custom message", exception.getMessage());
    }

}