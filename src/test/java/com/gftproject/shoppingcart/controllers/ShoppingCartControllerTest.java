package com.gftproject.shoppingcart.controllers;

import com.gftproject.shoppingcart.CartsData;
import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;
import com.gftproject.shoppingcart.services.ShoppingCartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class ShoppingCartControllerTest {

    @InjectMocks
    ShoppingCartController controller;

    @Mock
    ShoppingCartServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ShoppingCartController(service);
    }

    @Test
    @DisplayName("Find All Carts")
    void findAllByEmptyStatus() {

        given(service.findAllByStatus(any())).willReturn(CartsData.getMockCarts());
        given(service.findAll()).willReturn(CartsData.getMockCarts());

        ResponseEntity<List<Cart>> response = controller.findAllByStatus(null);

        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        verify(service, never()).findAllByStatus(any());

    }

    @Test
    @DisplayName("Find all carts with filter")
    void findAllByStatus() {

        given(service.findAllByStatus(any())).willReturn(CartsData.getMockCarts());
        given(service.findAll()).willReturn(CartsData.getMockCarts());

        ResponseEntity<List<Cart>> response = controller.findAllByStatus(Status.SUBMITTED);

        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        verify(service).findAllByStatus(any());
    }

    @Test
    @DisplayName("Create a shopping cart")
    void createShoppingCart() {
        given(service.createCart(any())).willReturn(CartsData.createCart001());

        ResponseEntity<Cart> cart = controller.createShoppingCart(1L);

        assertNotNull(cart.getBody());
        assertEquals(HttpStatusCode.valueOf(201), cart.getStatusCode());
        verify(service).createCart(any());

    }

    @Test
    @DisplayName("Submit a cart")
    void submitCart() {
        given(service.submitCart(any())).willReturn(CartsData.createSampleCart());

        ResponseEntity<Cart> cart = controller.submitCart(1L);

        assertNotNull(cart.getBody());
        assertEquals(HttpStatusCode.valueOf(200), cart.getStatusCode());
        verify(service).submitCart(any());
    }

    @Test
    void addToCart() throws Exception{
        given(service.submitCart(any())).willReturn(CartsData.createSampleCart());
    }

//    @Test
//    void deleteShoppingCart() throws Exception {
//
//        //deleteCart001 CartsData.java method?
//        given(service.deleteCart(any())).willReturn(CartsData.createCart001().orElseThrow());
//
//        mvc.perform(delete("/carts/1")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//
//        verify(service).deleteCart(any());
//    }
}