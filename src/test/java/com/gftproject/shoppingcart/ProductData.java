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
        products.put(2L, 4);
        products.put(3L, 4);
        products.put(4L, 2);
        products.put(5L, 6);

        return products;
    }

    public static List<Product> getWarehouseStock() {

        List<Product> products = new ArrayList<>();

        products.add(createProduct001());
        products.add(createProduct002());
        products.add(createProduct003());
        products.add(createProduct004());
        products.add(createProduct005());

        return products;
    }
}