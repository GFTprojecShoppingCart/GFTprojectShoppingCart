package com.gftproject.shoppingcart.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(Long userId) {
        super("User not found in warehouse: " + userId);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
