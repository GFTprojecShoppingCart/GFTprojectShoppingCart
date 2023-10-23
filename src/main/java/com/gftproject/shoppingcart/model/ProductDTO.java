package com.gftproject.shoppingcart.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ProductDTO {
    @NotNull
    private long id;
    @NotNull
    private BigDecimal price;
    @NotNull
    private int stock;
    @NotNull
    private BigDecimal weight;
}
