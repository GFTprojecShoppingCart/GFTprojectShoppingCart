package com.gftproject.shoppingcart.controllers;

import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ShoppingCartControllerAdvice {

    @ExceptionHandler(NotEnoughStockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleNotEnoughStockException(NotEnoughStockException ex) {

        String errorMessage = ex.getMessage();
        ErrorResponse errorResponse = new ErrorResponse("NOT ENOUGH STOCK ERROR", errorMessage);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex) {

        String errorMessage = ex.getMessage();
        ErrorResponse errorResponse = new ErrorResponse("PRODUCT NOT FOUND ERROR", errorMessage);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    
}
