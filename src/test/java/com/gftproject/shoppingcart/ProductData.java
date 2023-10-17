package com.gftproject.shoppingcart;

import com.gftproject.shoppingcart.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductData {

    public static Product createProduct001() {
        return new Product(1L, new BigDecimal("25.99"), new BigDecimal("55"), 3);
    }
    public static Product createProduct002() {
        return new Product(2L , new BigDecimal("19.99"), new BigDecimal("1.2"), 4);
    }
    public static Product createProduct003() {
        return new Product(3L , new BigDecimal("12.5"), new BigDecimal("0.7"), 5);
    }
    public static Product createProduct004() {
        return new Product(4L , new BigDecimal("7.99"), new BigDecimal("0.5"), 6);
    }
    public static Product createProduct005() {
        return new Product(5L , new BigDecimal("14.99"),  new BigDecimal("0.3"), 1);
    }

    public static Map<Long, Integer> getMockProductMap(){
        Map<Long, Integer> products = new HashMap<>();

        products.put(1L, 2);
        products.put(2L, 2);
        products.put(3L, 1);
        products.put(4L, 2);

        return products;
    }

    public static Map<Long, Integer> getLowWarehouseStock() {

        Map<Long, Integer> products = new HashMap<>();

        products.put(createProduct001().getId(), 2);
        products.put(createProduct002().getId(), 2);
        products.put(createProduct003().getId(), 1);
        products.put(createProduct005().getId(), 4);

        return products;
    }

    public static Map<Long, Integer> getHighWarehouseStock() {

        Map<Long, Integer> products = new HashMap<>();

        products.put(createProduct001().getId(), 20);
        products.put(createProduct002().getId(), 20);
        products.put(createProduct003().getId(), 10);
        products.put(createProduct005().getId(), 40);

        return products;
    }

    public static List<Product> getWarehouseStock() {

        List<Product> products = new ArrayList<>();

        products.add(createProduct001());
        products.add(createProduct002());
        products.add(createProduct003());
        products.add(createProduct005());

        return products;
    }
}