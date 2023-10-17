package com.gftproject.shoppingcart.model;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;

@Data
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Min(value = 0, message = "Final weight should be zero or greater")
    private BigDecimal price;
    @Min(value = 0, message = "Final weight should be zero or greater")
    private BigDecimal weight;
    @Min(value = 0, message = "Final weight should be zero or greater")
    private int storageQuantity;

}

