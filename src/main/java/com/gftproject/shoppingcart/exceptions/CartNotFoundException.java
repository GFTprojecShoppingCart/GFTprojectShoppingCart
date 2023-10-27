package com.gftproject.shoppingcart.exceptions;

import com.gftproject.shoppingcart.model.Cart;

import java.util.List;

public class CartNotFoundException extends Exception{

    public CartNotFoundException(String message){
        super(message);
    }
}

