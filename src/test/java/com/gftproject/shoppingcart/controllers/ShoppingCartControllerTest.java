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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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

        assertThat(response.getBody()).isNotNull().hasSize(4);
        verify(service, never()).findAllByStatus(any());

    }

    @Test
    @DisplayName("GIVEN a status as parameter WHEN the controller is called THEN returns a filtered list of carts")
    void findAllByStatus() {

        given(service.findAllByStatus(any())).willReturn(List.of(CartsData.createCart004()));
        given(service.findAll()).willReturn(CartsData.getMockCarts());

        ResponseEntity<List<Cart>> response = controller.findAllByStatus(Status.SUBMITTED);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get(0).getStatus()).isEqualTo(Status.SUBMITTED);
        assertThat(response.getBody()).hasSize(1);
        verify(service).findAllByStatus(any());
    }

    @Test
    @DisplayName("GIVEN the userID WHEN method called THEN returns the created cart")
    void createCart() {
        given(service.createCart(any())).willReturn(CartsData.createCart001());

        ResponseEntity<Cart> cart = controller.createCart("1");

        assertThat(cart.getBody()).isNotNull();
        assertThat(cart.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        verify(service).createCart(any());
    }

    @Test
    @DisplayName("GIVEN the userID WHEN method called THEN returns the created cart")
    void createCartBadRequest() {
        given(service.createCart(any())).willReturn(CartsData.createCart001());

        ResponseEntity<Cart> cart = controller.createCart("Oof");

        assertThat(cart.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
        verify(service, never()).createCart(any());
    }

    @Test
    @DisplayName("GIVEN a cartId WHEN the controller is called THEN the cart status will change to submitted")
    void submitCart() throws NotEnoughStockException, ProductNotFoundException {
        given(service.submitCart(any())).willReturn(CartsData.createCart001());

        ResponseEntity<Cart> cart = controller.submitCart("1");

        assertThat(cart.getBody()).isNotNull();
        assertThat(cart.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        verify(service).submitCart(any());
    }

    @Test
    @DisplayName("GIVEN a list of products WHEN the method is called THEN the list of products without enough stock will be updated")
    void updateProductsFromCarts() {
        given(service.updateProductsFromCarts(any())).willReturn(CartsData.getMockCarts());

        ResponseEntity<List<Cart>> response = controller.updateProductsFromCarts(ProductData.getWarehouseStock());

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        verify(service).updateProductsFromCarts(any());
    }

    @Test
    @DisplayName("WHEN deleteCart is executed THEN Delete a cart object")
    void deleteCart() {
        // When
        ResponseEntity<Void> response = controller.deleteShoppingCart(1L);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.valueOf(200));

        // Verification
        verify(service).deleteCart(1L);
    }

    @Test
    @DisplayName("GIVEN cartId, product and quantity WHEN product is added THEN response is OK")
    void testAddProductToCartWithQuantity() throws ProductNotFoundException {

        when(service.addProductToCartWithQuantity(anyLong(), anyLong(), anyInt())).thenReturn(CartsData.createCart001());

        ResponseEntity<Cart> responseEntity = controller.addProductToCart(1L, 1L, 3);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    }
}
