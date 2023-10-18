package com.gftproject.shoppingcart.controllers;

import com.gftproject.shoppingcart.CartsData;
import com.gftproject.shoppingcart.ProductData;
import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;
import com.gftproject.shoppingcart.services.ShoppingCartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
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
    @DisplayName("WHEN the controller is called THEN returns all carts")
    void findAllByEmptyStatus() {

        given(service.findAllByStatus(any())).willReturn(CartsData.getMockCarts());
        given(service.findAll()).willReturn(CartsData.getMockCarts());

        ResponseEntity<List<Cart>> response = controller.findAllByStatus(null);

        assertNotNull(response.getBody());
        assertEquals(4, response.getBody().size());
        verify(service, never()).findAllByStatus(any());

    }

    @Test
    @DisplayName("GIVEN a status as parameter WHEN the controller is called THEN returns a filtered list of carts")
    void findAllByStatus() {

        given(service.findAllByStatus(any())).willReturn(List.of(CartsData.createCart004()));
        given(service.findAll()).willReturn(CartsData.getMockCarts());

        ResponseEntity<List<Cart>> response = controller.findAllByStatus(Status.SUBMITTED);

        assertNotNull(response.getBody());
        assertEquals(Status.SUBMITTED, response.getBody().get(0).getStatus());
        assertEquals(1, response.getBody().size());
        verify(service).findAllByStatus(any());
    }

    @Test
    @DisplayName("GIVEN the userID WHEN method called THEN returns the created cart")
    void createCart() {
        given(service.createCart(any())).willReturn(CartsData.createCart001());

        ResponseEntity<Cart> cart = controller.createCart("1");

        assertNotNull(cart.getBody());
        assertEquals(HttpStatusCode.valueOf(201), cart.getStatusCode());
        verify(service).createCart(any());
    }

    @Test
    @DisplayName("GIVEN a cartId WHEN the controller is called THEN the cart status will change to submitted")
    void submitCart() throws NotEnoughStockException, ProductNotFoundException {
        given(service.submitCart(any())).willReturn(CartsData.createCart001());

        ResponseEntity<Cart> cart = controller.submitCart("1");

        assertNotNull(cart.getBody());
        assertEquals(HttpStatusCode.valueOf(200), cart.getStatusCode());
        verify(service).submitCart(any());
    }

    @Test
    @DisplayName("GIVEN a list of products WHEN the method is called THEN the list of products without enough stock will be updated")
    void updateProductsFromCarts() {
        given(service.updateProductsFromCarts(any())).willReturn(CartsData.getMockCarts());

        ResponseEntity<List<Cart>> response = controller.updateProductsFromCarts(ProductData.getWarehouseStock());

        assertNotNull(response.getBody());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        verify(service).updateProductsFromCarts(any());
    }

    @Test
    @DisplayName("WHEN deleteCart is executed THEN Delete a cart object")
    void deleteCart() {
        // When
        ResponseEntity<Void> response = controller.deleteShoppingCart(1L);

        // Then
        assertEquals(HttpStatus.valueOf(200), response.getStatusCode());

        // Verification
        verify(service).deleteCart(1L);
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