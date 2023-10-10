package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;
import com.gftproject.shoppingcart.repositories.ShoppingCartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static com.gftproject.shoppingcart.CartsData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(ShoppingCartService.class)
class ShoppingCartServiceTest {
    @Autowired
    ShoppingCartService service;

    @MockBean
    ShoppingCartRepository shoppingCartRepository;

    @Test
    void getCartsByStatus() {
        //Given
        List<Cart> carts = Arrays.asList(createCart001().orElseThrow(), createCart002().orElseThrow(),createCart003().orElseThrow());
        when(shoppingCartRepository.findAllByStatus(any())).thenReturn(carts);

        //when
        List<Cart> allCarts = service.findAllByStatus(Status.DRAFT);

        //then
        assertNotNull(allCarts);
        assertEquals(3, allCarts.size());
        verify(shoppingCartRepository).findAllByStatus(Status.DRAFT);
    }

    // TODO




}