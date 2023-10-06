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
        return Optional.of(new Product("Balon de futbol", "Balon de futbol profesional", "Deportes", 25.99, 0.4, 50));
    }

    public static Optional<Product> createProduct002() {
        return Optional.of(new Product("Rompecabezas 1000 piezas", "Rompecabezas con ilustracion de paisaje", "Juguetes", 19.99, 1.2, 30));
    }

    public static Optional<Product> createProduct003() {
        return Optional.of(new Product("Libro El Gran Gatsby", "Novela clasica de F. Scott Fitzgerald", "Libros", 12.5, 0.7, 20));
    }
    public static Optional<Product> createProduct004() {
        return Optional.of(new Product("Paquete de Pasta Italiana", "Pasta de trigo de alta calidad", "Comida", 7.99, 0.5, 100));
    }
    public static Optional<Product> createProduct005() {
        return Optional.of(new Product("Camiseta de Algodon", "Camiseta de manga corta, color negro", "Ropa", 14.99, 0.3, 60));
    }
}