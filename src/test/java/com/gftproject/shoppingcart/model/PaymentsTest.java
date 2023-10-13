package com.gftproject.shoppingcart.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentsTest {

    @Test
    public void testGettersAndSetters() {
        Payments payment = new Payments();
        payment.setPaymentMethod("CreditCard");
        payment.setChargePercentage(2.5);

        assertEquals("CreditCard", payment.getPaymentMethod());
        assertEquals(2.5, payment.getChargePercentage(), 0.01);
    }

    @Test
    public void testNoArgsConstructor() {
        Payments payment = new Payments();
        assertNotNull(payment);
    }

    @Test
    public void testAllArgsConstructor() {
        Payments payment = new Payments("PayPal", 3.0);

        assertEquals("PayPal", payment.getPaymentMethod());
        assertEquals(3.0, payment.getChargePercentage(), 0.01);
    }

    @Test
    public void testId() {
        Payments payment = new Payments();
        payment.setPaymentMethod("Bitcoin");

        assertEquals("Bitcoin", payment.getPaymentMethod());
    }
}