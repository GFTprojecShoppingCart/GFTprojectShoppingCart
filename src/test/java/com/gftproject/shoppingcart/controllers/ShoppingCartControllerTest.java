package com.gftproject.shoppingcart.controllers;

import com.gftproject.shoppingcart.CartsData;
import com.gftproject.shoppingcart.ProductData;
import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.exceptions.UserNotFoundException;
import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.CartProduct;
import com.gftproject.shoppingcart.model.ProductDTO;
import com.gftproject.shoppingcart.model.Status;
import com.gftproject.shoppingcart.services.ShoppingCartService;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    @DisplayName("GIVEN a call of controller WHEN findAllByEmptyStatus THEN returns all carts")
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
    void createCart() throws UserNotFoundException {
        given(service.createCart(any())).willReturn(CartsData.createCart001());

        ResponseEntity<Cart> cart = controller.createCart("1");

        assertThat(cart.getBody()).isNotNull();
        assertThat(cart.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        verify(service).createCart(any());
    }

    @Test
    @DisplayName("GIVEN the userID WHEN method called THEN returns the created cart")
    void createCartBadRequest() throws UserNotFoundException {
        given(service.createCart(any())).willReturn(CartsData.createCart001());

        ResponseEntity<Cart> cart = controller.createCart("Oof");

        assertThat(cart.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
        verify(service, never()).createCart(any());
    }

    @Test
    @DisplayName("GIVEN a cartId WHEN the controller is called THEN the cart status will change to submitted")
    void submitCart() throws NotEnoughStockException, ProductNotFoundException, UserNotFoundException {
        given(service.submitCart(any())).willReturn(CartsData.createCart001());

        ResponseEntity<Cart> cart = controller.submitCart("1");

        assertThat(cart.getBody()).isNotNull();
        assertThat(cart.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        verify(service).submitCart(any());
    }

    @Test
    @DisplayName("GIVEN a cartId WHEN the controller is called THEN a BadRequest httpStatus will be returned")
    void submitCartBadRequest() throws NotEnoughStockException, ProductNotFoundException, UserNotFoundException {
        given(service.submitCart(any())).willReturn(CartsData.createCart001());

        ResponseEntity<Cart> cart = controller.submitCart("A");

        assertThat(cart.getBody()).isNull();
        assertThat(cart.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(service, times(0)).submitCart(any());
    }

    @Test
    @DisplayName("GIVEN a list of products WHEN the method is called THEN the list of products without enough stock will be updated")
    void updateProductsFromCarts() {
        List<ProductDTO> productDataList = ProductData.getWarehouseStock();

        ResponseEntity<Void> response = controller.updateProductsFromCarts(ProductData.getWarehouseStock());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        verify(service).updateProductsFromCarts(any());
    }

    @Test
    @DisplayName("GIVEN cartId WHEN deleteCart THEN delete a cart object")
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
    void testAddProductToCartWithQuantity() throws ProductNotFoundException, NotEnoughStockException {

        when(service.addProductToCartWithQuantity(anyLong(), anyLong(), anyLong(), anyInt())).thenReturn(CartsData.createCart001());

        ResponseEntity<Cart> responseEntity = controller.addProductToCart(1L, 1L, 3L, 4);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    @DisplayName("GIVEN userId, cartId, and productId WHEN product is deleted from cart THEN response is OK")
    void deleteProductFromCart() {

        ResponseEntity<Void> response = controller.deleteProductFromCart(1L, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(service).deleteProductFromCart(anyLong(), anyLong());
    }
}
