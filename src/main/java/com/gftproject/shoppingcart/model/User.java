package com.gftproject.shoppingcart.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class User {
    long userID;
    String country;
    String paymentMethod;

}
