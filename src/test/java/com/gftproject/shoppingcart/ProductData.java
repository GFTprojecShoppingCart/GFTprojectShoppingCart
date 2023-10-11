package com.gftproject.shoppingcart;

import com.gftproject.shoppingcart.model.Product;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ProductData {

    public static Product createProduct001() {
        return new Product(1L, new BigDecimal("25.99"), "Balon de futbol", new BigDecimal("55"), 3);
    }
    public static Product createProduct002() {
        return new Product(2L , new BigDecimal("19.99"), "Rompecabezas 1000 piezas", new BigDecimal("1.2"), 4);
    }
    public static Product createProduct003() {
        return new Product(3L , new BigDecimal("12.5"),"Libro El Gran Gatsby", new BigDecimal("0.7"), 5);
    }
    public static Product createProduct004() {
        return new Product(4L , new BigDecimal("7.99"), "Paquete de Pasta Italiana", new BigDecimal("0.5"), 6);
    }
    public static Product createProduct005() {
        return new Product(5L , new BigDecimal("14.99"),"Camiseta de Algodon",  new BigDecimal("0.3"), 1);
    }

    public static Map<Product, Integer> getMockProductMap(){
        Map<Product, Integer> products = new HashMap<>();

        products.put(createProduct001(), 2);
        products.put(createProduct002(), 2);
        products.put(createProduct003(), 1);
        products.put(createProduct004(), 2);

        return products;
    }

    public static Map<Product, Integer> getFaultyMockProductMap() {

        Map<Product, Integer> products = new HashMap<>();

        products.put(createProduct001(), 2);
        products.put(createProduct002(), 2);
        products.put(createProduct003(), 1);
        products.put(createProduct005(), 4);

        return products;
    }
}