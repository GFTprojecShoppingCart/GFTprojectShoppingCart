package com.gftproject.shoppingcart;

import com.gftproject.shoppingcart.model.Product;

import java.util.HashMap;
import java.util.Map;

public class ProductData {

    public static Product createProduct001() {
        return new Product(1L, 25.99, "Balon de futbol", 55, 1);
    }

    public static Product createProduct002() {
        return new Product(2L , 19.99, "Rompecabezas 1000 piezas", 1.2, 1);
    }

    public static Product createProduct003() {
        return new Product(3L , 12.5,"Libro El Gran Gatsby", 0.7, 1);
    }
    public static Product createProduct004() {
        return new Product(4L , 7.99, "Paquete de Pasta Italiana", 0.5, 1);
    }
    public static Product createProduct005() {
        return new Product(5L , 14.99,"Camiseta de Algodon",  0.3, 1);
    }
    public static Map<Product, Integer> getMockProductMap(){
        Map<Product, Integer> products = new HashMap<>();

        products.put(createProduct001(), 2);
        products.put(createProduct002(), 2);
        products.put(createProduct003(), 2);
        products.put(createProduct004(), 2);

        return products;
    }
}