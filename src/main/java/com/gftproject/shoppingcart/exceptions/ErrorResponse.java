package com.gftproject.shoppingcart.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String error;
    private String message;

}
