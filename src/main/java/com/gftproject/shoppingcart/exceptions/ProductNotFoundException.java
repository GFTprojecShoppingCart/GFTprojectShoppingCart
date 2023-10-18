package com.gftproject.shoppingcart.exceptions;

public class ProductNotFoundException extends Exception{
    public ProductNotFoundException(List<Long> productId){
        super("Products not found in warehouse: " + productId);
    }

    public ProductNotFoundException(String message){
        super(message);
    }
}
