package com.gftproject.shoppingcart.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@IdClass(CartProductPK.class)
@AllArgsConstructor
@NoArgsConstructor
public class CartProduct {

    @Id
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Id
    private Long product;

    boolean valid;

    @Min(value = 0, message = "Product quantity should be zero or greater")
    private int quantity;

}
