package com.gftproject.shoppingcart.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryTest {

    @Test
    public void testGettersAndSetters() {
        Country country = new Country();
        country.setName("TestCountry");
        country.setTaxPercentage(10.0);

        assertEquals("TestCountry", country.getName());
        assertEquals(10.0, country.getTaxPercentage(), 0.01);
    }

    @Test
    public void testNoArgsConstructor() {
        Country country = new Country();
        assertNotNull(country);
    }

    @Test
    public void testAllArgsConstructor() {
        Country country = new Country("TestCountry", 10.0);

        assertEquals("TestCountry", country.getName());
        assertEquals(10.0, country.getTaxPercentage(), 0.01);
    }

    @Test
    public void testId() {
        Country country = new Country();
        country.setName("TestCountry");

        assertEquals("TestCountry", country.getName());
    }
}