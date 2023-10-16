package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Product;
import com.gftproject.shoppingcart.model.Status;
import com.gftproject.shoppingcart.repositories.ShoppingCartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static com.gftproject.shoppingcart.CartsData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class ShoppingCartServiceTest {
    @InjectMocks
    ShoppingCartServiceImpl service;

    @Mock
    ShoppingCartRepository shoppingCartRepository;

    @Mock
    CartComputationsService computationsService;

    private List<Cart> carts;

    @BeforeEach
    void setUp() {
        // Instanciar Shopping cart Service y istanciar con new mock de repository
        MockitoAnnotations.openMocks(this);
        service = new ShoppingCartServiceImpl(shoppingCartRepository, computationsService);
        carts = Arrays.asList(Optional.of(createCart001()).orElseThrow(), createCart002(),createCart003());
    }

    @Test
    @DisplayName("Gets a full list of carts")
    void getAllCarts() {
        //Given
        when(shoppingCartRepository.findAll()).thenReturn(carts);

        //when
        List<Cart> allCarts = service.findAll();

        //then
        assertNotNull(allCarts);
        assertEquals(3, allCarts.size());
        verify(shoppingCartRepository).findAll();
    }

    @Test
    @DisplayName("Find all carts")
    void getCartsByStatus() {
        //Given
        when(shoppingCartRepository.findAllByStatus(any())).thenReturn(carts);

        //when
        List<Cart> allCarts = service.findAllByStatus(Status.DRAFT);

        //then
        assertThat(allCarts)
            .isNotNull()
            .hasSize(3);

        verify(shoppingCartRepository).findAllByStatus(Status.DRAFT);
    }

    @Test
    @DisplayName("Submit a cart")
    void submitCart(){
        when(shoppingCartRepository.findById(any())).thenReturn(Optional.of(createCart001()));
        when(shoppingCartRepository.save(any())).thenReturn(createSampleCart());
        //when(computationsService.computeFinalValues(any()));

        Cart submittedCart = service.submitCart(1L);

        // Verify that the service method correctly calls the repository
        verify(shoppingCartRepository).findById(1L);
        verify(shoppingCartRepository).save(any());

        assertThat(submittedCart).isNotNull();
        assertThat(submittedCart.getFinalPrice()).isNotZero();
        assertThat(submittedCart.getStatus()).isEqualTo(Status.SUBMITTED);
        assertThat(submittedCart.getId()).isEqualTo(1L);
    }


    @Test
    @DisplayName("Add product to cart and check stock")
    void addProductWithQuantity(){
        Cart cart = new Cart(1L, new HashMap<>(), 1L, Status.DRAFT,new BigDecimal(14), BigDecimal.ZERO);
        Product product = new Product(1L, new BigDecimal(3), "Producto de prueba", new BigDecimal("0.5"), 5);
        when(shoppingCartRepository.findById(any())).thenReturn(Optional.of(cart));
        when(computationsService.checkStock(cart.getProducts())).thenReturn(true);
        when(shoppingCartRepository.save(any())).thenReturn(cart);
        Cart updatedCart = service.addProductToCartWithQuantity(1L, product, 5);
        assertNotNull(updatedCart);

        assertEquals(1L, updatedCart.getId());
        verify(shoppingCartRepository).save(cart);
    }


    @Test
    @DisplayName("GIVEN cartId WHEN deleteCart is executed THEN Delete a cart object")
    void deleteCart() {
        // Given
        doNothing().when(shoppingCartRepository).deleteById(1L);

        // When
        service.deleteCart(1L);

        // Then
        verify(shoppingCartRepository).deleteById(eq(1L));
    }
}





