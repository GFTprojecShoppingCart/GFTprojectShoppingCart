package com.gftproject.shoppingcart.exceptions;

public class NotEnoughStockException extends Exception {
    public NotEnoughStockException(){
        super("Not enough stock available");
    }

    public NotEnoughStockException(String message){
        super(message);
    }
}
