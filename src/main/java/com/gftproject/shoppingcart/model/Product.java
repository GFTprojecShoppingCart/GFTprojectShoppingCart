package com.gftproject.shoppingcart.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Product")
@Data
@AllArgsConstructor @NoArgsConstructor
public class Product {
    @Id
    private long id;
    private int quantity;
    private int storageQuantity;
}

