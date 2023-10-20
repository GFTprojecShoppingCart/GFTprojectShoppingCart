package com.gftproject.shoppingcart.model;


import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor @NoArgsConstructor
public class Product {
    private long id;
    private BigDecimal price;
    private BigDecimal weight;
    private int storageQuantity;
}

