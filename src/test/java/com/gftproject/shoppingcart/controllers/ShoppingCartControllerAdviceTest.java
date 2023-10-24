package com.gftproject.shoppingcart.controllers;


import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.exceptions.UserNotFoundException;
import com.gftproject.shoppingcart.model.ErrorResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShoppingCartControllerAdviceTest {
    @InjectMocks
    ShoppingCartControllerAdvice controllerAdvice;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controllerAdvice = new ShoppingCartControllerAdvice();
    }

    @Test
    @DisplayName("GIVEN a NotEnoughStockException WHEN handling the exception in ShoppingCartControllerAdvice THEN it should return a ResponseEntity with the correct error message and status code")
    void testHandleNotEnoughStockException() {


        // Crear una lista de IDs de productos de ejemplo
        List<Long> productIds = Arrays.asList(1L, 2L, 3L);

        // Crear una instancia de NotEnoughStockException
        NotEnoughStockException exception = new NotEnoughStockException(productIds);

        // Llamar al método handleNotEnoughStockException con la excepción
        ResponseEntity<ErrorResponse> responseEntity = controllerAdvice.handleNotEnoughStockException(exception);

        // Verificar que la respuesta no sea nula
        assertThat(responseEntity).isNotNull();

        // Verificar el código de estado HTTP de la respuesta
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        // Verificar el mensaje de la respuesta
        ErrorResponse errorResponse = responseEntity.getBody();
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getError()).isEqualTo("NOT ENOUGH STOCK ERROR");
        assertThat(errorResponse.getMessage()).isEqualTo("Not enough stock of products with Id: [1, 2, 3]");
    }

    @Test
    @DisplayName("GIVEN a ProductNotFoundException WHEN handling the exception in ShoppingCartControllerAdvice THEN it should return a ResponseEntity with the correct error message and status code")
    void testHandleProductNotFoundException() {
        ProductNotFoundException exception = new ProductNotFoundException(List.of(1L));

        ResponseEntity<ErrorResponse> responseEntity = controllerAdvice.handleProductNotFoundException(exception);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ErrorResponse errorResponse = responseEntity.getBody();
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getError()).isEqualTo("PRODUCT NOT FOUND ERROR");
        assertThat(errorResponse.getMessage()).isEqualTo("Products not found in warehouse: [1]");
    }

    @Test
    @DisplayName("GIVEN a UserNotFoundException WHEN handling the exception in ShoppingCartControllerAdvice THEN it should return a ResponseEntity with the correct error message and status code")
    void testHandleUserNotFoundException() {
        UserNotFoundException exception = new UserNotFoundException(1L);

        ResponseEntity<ErrorResponse> responseEntity = controllerAdvice.handleUserNotFoundException(exception);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ErrorResponse errorResponse = responseEntity.getBody();
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getError()).isEqualTo("USER NOT FOUND ERROR");
        assertThat(errorResponse.getMessage()).isEqualTo("User not found in warehouse: 1");
    }

}
