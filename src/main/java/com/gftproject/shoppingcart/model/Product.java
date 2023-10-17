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
    private long id;
    private BigDecimal price;
    private BigDecimal weight;
    private int storageQuantity;
}

