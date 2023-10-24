package com.gftproject.shoppingcart.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "The user ID must be provided")
    private long userId;

/*    @OneToMany(mappedBy = "cart")
    private List<CartProduct> cartProducts;*/

    @Enumerated(EnumType.STRING)
    private Status status;

    @Min(value = 0, message = "Final price should be zero or greater")
    private BigDecimal finalPrice;

    @Min(value = 0, message = "Final weight should be zero or greater")
    private BigDecimal finalWeight;

    public Cart(long userId, Status status, BigDecimal finalPrice, BigDecimal finalWeight) {
        this.userId = userId;
        this.status = status;
        this.finalPrice = finalPrice;
        this.finalWeight = finalWeight;
    }
}
