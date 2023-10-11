package com.gftproject.shoppingcart;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Product;
import com.gftproject.shoppingcart.model.Status;

import java.util.Optional;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Product;
import com.gftproject.shoppingcart.model.Status;

public class ProductData {

    public static Optional<Product> createProduct001() {
        return Optional.of(new Product(1L, 25.99, "Balon de futbol", 55));
    }

    public static Optional<Product> createProduct002() {
        return Optional.of(new Product(2L , 19.99, "Rompecabezas 1000 piezas", 1.2));
    }

    public static Optional<Product> createProduct003() {
        return Optional.of(new Product(3L , 12.5,"Libro El Gran Gatsby", 0.7));
    }
    public static Optional<Product> createProduct004() {
        return Optional.of(new Product(4L , 7.99, "Paquete de Pasta Italiana", 0.5));
    }
    public static Optional<Product> createProduct005() {
        return Optional.of(new Product(5L , 14.99,"Camiseta de Algodon",  0.3));
    }
}