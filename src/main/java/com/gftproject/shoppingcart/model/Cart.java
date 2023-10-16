package com.gftproject.shoppingcart.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
//@Getter @Setter @RequiredArgsConstructor -> already implemented by Data
@AllArgsConstructor @NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    List<Long> invalidProducts;
    
    @ElementCollection
    private Map<Long, Integer> products = new HashMap<>();

    @NotNull(message = "The user ID must be provided")
    private long userId;

    @Enumerated(EnumType.STRING) // Use EnumType.STRING to map the enum by its name
    private Status status;

    @Min(value = 0, message = "Final price should be zero or greater")
    private BigDecimal finalPrice;
    @Min(value = 0, message = "Final weight should be zero or greater")
    private BigDecimal finalWeight;

}

