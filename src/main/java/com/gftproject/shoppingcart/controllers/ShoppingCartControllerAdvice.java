package com.gftproject.shoppingcart.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.model.ErrorResponse;

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
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(NotEnoughStockException ex) {

        String errorMessage = ex.getMessage();
        ErrorResponse errorResponse = new ErrorResponse("NOT PRODUCT FOUND ERROR", errorMessage);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }



    // Convert to up format
    /*@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException ex) {
        String errorMessage = "Not allowed method for this request. Check path and method HTTP";
        return new ResponseEntity<>(errorMessage, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<String> handleNumberFormatException(NumberFormatException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Debes de ingresar un numero válido.");
    }*/
    
}
