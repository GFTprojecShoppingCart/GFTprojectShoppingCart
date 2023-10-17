package com.gftproject.shoppingcart.exceptions;

import java.util.List;

public class NotEnoughStockException extends RuntimeException {
    private final List<Long> productIds;

    public NotEnoughStockException(List<Long> productIds) {
        super("Not enough stock of products with Id: " + productIds);
        this.productIds = productIds;
    }

    public List<Long> getProductIds() {
        return productIds;
    }
}
