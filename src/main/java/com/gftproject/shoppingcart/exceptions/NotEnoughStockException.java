package com.gftproject.shoppingcart.exceptions;

import java.util.List;

public class NotEnoughStockException extends Exception {

    public NotEnoughStockException(List<Long> productIds) {
        super("Not enough stock of products with Id: " + productIds);
    }
}
