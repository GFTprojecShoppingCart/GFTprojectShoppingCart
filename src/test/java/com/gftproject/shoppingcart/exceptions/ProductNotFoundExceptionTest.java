package com.gftproject.shoppingcart.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductNotFoundExceptionTest {

    @Test
    void testProductNotFoundException() {

        // Crear una instancia de NotEnoughStockException
        ProductNotFoundException exception = new ProductNotFoundException(5L);

        // Verificar que la excepción no sea nula
        assertThat(exception).isNotNull();

        // Verificar que el mensaje de la excepción sea el esperado
        assertThat(exception.getMessage()).isEqualTo("The prodict of Id 5 was not found in the warehouse.");
    }

}