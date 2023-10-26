package com.gftproject.shoppingcart.exceptions;

public class CartIsAlreadySubmittedException extends Exception{

    public CartIsAlreadySubmittedException(Long cartId){
        super("The selected cart is already closed and cannot be modified: " + cartId);
    }
}
