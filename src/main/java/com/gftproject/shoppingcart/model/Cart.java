package com.gftproject.shoppingcart.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @ElementCollection
    private final Map<Product, Integer> products = new HashMap<>();

    private long user_id;

    @Enumerated(EnumType.STRING) // Use EnumType.STRING to map the enum by its name
    private Status status;

    private double finalPrice;
    private double finalWeight;

    public void computeFinalValues(){

        double totalWeight = 0.0;
        double totalPrice = 0.0;

        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            double productWeight = product.getWeight();
            double productPrice = product.getPrice();

            totalWeight += productWeight * quantity;
            totalPrice += productPrice * quantity;
        }

        this.finalWeight = totalWeight;
        this.finalPrice = totalPrice;
    }



}

