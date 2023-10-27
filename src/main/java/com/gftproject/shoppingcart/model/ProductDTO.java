package com.gftproject.shoppingcart.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonCreator
    public ProductDTO(@JsonProperty("id") long id, @JsonProperty("price") String price, @JsonProperty("stock") int stock, @JsonProperty("weight") String weight) {
        this.id = id;
        this.price = new BigDecimal(price);
        this.stock = stock;
        this.weight = new BigDecimal(weight);
    }
}
