package com.gftproject.shoppingcart.exceptions;

public class ProductNotFoundException extends Exception{
    public ProductNotFoundException(){
        super("Specified product not found in warehouse.");
    }

    public ProductNotFoundException(Long productId){

        super("The product of Id " + productId + " was not found in the warehouse.");
    }
}
