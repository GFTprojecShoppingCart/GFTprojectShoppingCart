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
        given(service.submitCart(any())).willReturn(CartsData.createCart001());

        ResponseEntity<Cart> cart = controller.submitCart(1L);

        assertNotNull(cart.getBody());
        assertEquals(HttpStatusCode.valueOf(200), cart.getStatusCode());
        verify(service).submitCart(any());
    }

    @Test
    @DisplayName("Update stock of a cart")
    void updateStockCart() {
        given(service.updateStockCart(any())).willReturn(CartsData.createCart001());

        ResponseEntity<Cart> cart = controller.updateStockCart(1L);

        assertNotNull(cart.getBody());
        assertEquals(HttpStatusCode.valueOf(200), cart.getStatusCode());
        verify(service).submitCart(any());
    }

    @Test
    void addToCart() throws Exception{
        //TODO
        given(service.submitCart(any())).willReturn(CartsData.createCart001());
    }

    @Test
    void updateProductsFromCarts() {
        given(service.updateProductsFromCarts(any())).willReturn(CartsData.getMockCarts());

        ResponseEntity<Cart> cart = controller.updateStockCart(1L);

        assertNotNull(cart.getBody());
        assertEquals(HttpStatusCode.valueOf(200), cart.getStatusCode());
        verify(service).updateProductsFromCarts(any());
    }
}