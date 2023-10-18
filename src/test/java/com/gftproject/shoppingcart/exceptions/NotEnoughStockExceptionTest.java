package com.gftproject.shoppingcart.exceptions;

import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


class NotEnoughStockExceptionTest {

    @Test
    void testNotEnoughStockException() {
        // Crear una lista de IDs de productos de ejemplo
        List<Long> productIds = Arrays.asList(1L, 2L, 3L);

        // Crear una instancia de NotEnoughStockException
        NotEnoughStockException exception = new NotEnoughStockException(productIds);

        // Verificar que la excepción no sea nula
        assertThat(exception).isNotNull();

        // Verificar que el mensaje de la excepción sea el esperado
        assertThat(exception.getMessage()).isEqualTo("Not enough stock of products with Id: [1, 2, 3]");
    }
}