package com.gftproject.shoppingcart;

import com.gftproject.shoppingcart.model.ProductDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductData {

    public static Product createProduct001() {
        return new Product(1L, 3);
    }

    public static Product createProduct002() {
        return new Product(2L, 4);
    }

    public static Product createProduct003() {
        return new Product(3L, 5);
    }

    public static Product createProduct004() {
        return new Product(4L, 6);
    }

    public static Product createProduct005() {
        return new Product(5L, 1);
    }

    public static ProductDTO createProductDTO001() {
        return new ProductDTO(1L, new BigDecimal("25.99"), 3, new BigDecimal("55"));
    }

    public static ProductDTO createProductDTO002() {
        return new ProductDTO(2L, new BigDecimal("19.99"), 4, new BigDecimal("1.2"));
    }

    public static ProductDTO createProductDTO003() {
        return new ProductDTO(3L, new BigDecimal("12.5"), 5, new BigDecimal("0.7"));
    }

    public static ProductDTO createProductDTO004() {
        return new ProductDTO(4L, new BigDecimal("7.99"), 6, new BigDecimal("0.5"));
    }

    public static ProductDTO createProductDTO005() {
        return new ProductDTO(5L, new BigDecimal("14.99"), 1, new BigDecimal("0.3"));
    }

    public static List<Product> getMockProductMap() {
        List<Product> products = new ArrayList<>();

        products.add(createProduct001());
        products.add(createProduct002());
        products.add(createProduct003());
        products.add(createProduct004());
        products.add(createProduct005());

        return products;
    }

    public static List<ProductDTO> getWarehouseStock() {

        List<ProductDTO> products = new ArrayList<>();

        products.add(createProductDTO001());
        products.add(createProductDTO002());
        products.add(createProductDTO003());
        products.add(createProductDTO004());
        products.add(createProductDTO005());

        return products;
    }
}