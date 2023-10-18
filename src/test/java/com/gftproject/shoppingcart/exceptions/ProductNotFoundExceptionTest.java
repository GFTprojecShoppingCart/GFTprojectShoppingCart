package com.gftproject.shoppingcart.exceptions;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductNotFoundExceptionTest {

    @Test
    void testProductNotFoundException() {

        // Crear una instancia de NotEnoughStockException
        ProductNotFoundException exception = new ProductNotFoundException(List.of(5L));

        // Verificar que la excepción no sea nula
        assertThat(exception).isNotNull();

        // Verificar que el mensaje de la excepción sea el esperado
        assertThat(exception.getMessage()).isEqualTo("Products not found in warehouse: [5]");
    }

}