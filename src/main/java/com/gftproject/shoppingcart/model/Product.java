package com.gftproject.shoppingcart.model;

import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Product {
    Long id;
    int quantity;
    double price;
}
