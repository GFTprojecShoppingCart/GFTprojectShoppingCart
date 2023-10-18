package com.gftproject.shoppingcart.exceptions;

import java.util.List;

public class NotEnoughStockException extends Exception {

    public NotEnoughStockException(List<Long> productId){
        super("Not enough stock of products with Id: " + productId);
    }

    public NotEnoughStockException(String message) {
        super(message);

    }
}