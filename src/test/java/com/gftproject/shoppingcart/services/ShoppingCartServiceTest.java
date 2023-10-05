package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.CartsData.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;
import com.gftproject.shoppingcart.repositories.ShoppingCartRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class ShoppingCartServiceTest {
    @Autowired
    ShoppingCartService service;

    @MockBean
    ShoppingCartRepository shoppingCartRepository;

    @Test
    void getCartsByStatus() {
        //Given
        List<Cart> carts = Arrays.asList(createCart001().orElseThrow(), createCart002().orElseThrow(),createCart003().orElseThrow());
        when(shoppingCartRepository.findAllByStatus(Status.DRAFT)).thenReturn(carts);

        //when
        List<Cart> allCarts = service.findAllByStatus(Status.DRAFT);

        //then
        assertEquals(3, allCarts.size());


    }
}