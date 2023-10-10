package com.gftproject.shoppingcart.model;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double precio;
    private String nombre;
    private String categoria;
    private String descripcion;
    private double peso;
    private int stock;
    public Product(String nombre, String descripcion, String categoria,  double precio, double peso, int stock) {

    }
}