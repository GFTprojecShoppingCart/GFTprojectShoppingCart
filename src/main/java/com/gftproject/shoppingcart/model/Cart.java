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

    private double price;



}

