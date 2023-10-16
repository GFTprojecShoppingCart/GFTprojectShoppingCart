package com.gftproject.shoppingcart.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Min(value = 0, message = "Final weight should be zero or greater")
    private BigDecimal price;
    private String name;
    @Min(value = 0, message = "Final weight should be zero or greater")
    private BigDecimal weight;
    @Min(value = 0, message = "Final weight should be zero or greater")
    private int storageQuantity;
}

