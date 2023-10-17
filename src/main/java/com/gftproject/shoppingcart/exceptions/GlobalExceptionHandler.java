package com.gftproject.shoppingcart.exceptions;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotEnoughStockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleNotEnoughStockException(NotEnoughStockException ex) {
        List<Long> productIds = ex.getProductIds();
        String errorMessage = "Not enough stock of " + productIds.toString();
        ErrorResponse errorResponse = new ErrorResponse("Not enough stock", errorMessage);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
    
}
