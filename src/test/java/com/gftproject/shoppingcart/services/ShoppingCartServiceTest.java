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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.gftproject.shoppingcart.CartsData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;


class ShoppingCartServiceTest {
    @InjectMocks
    ShoppingCartServiceImpl service;

    @Mock
    ShoppingCartRepository shoppingCartRepository;

    @Mock
    CartComputationsService computationsService;

    @BeforeEach
    void setUp() {
        // Instanciar Shopping cart Service y istanciar con new mock de repository
        MockitoAnnotations.openMocks(this);
        service = new ShoppingCartServiceImpl(shoppingCartRepository, computationsService);
    }

    @Test
    @DisplayName("Get a filtered list by status")
    void getCartsByStatus() {
        //Given
        List<Cart> carts = Arrays.asList(Optional.of(createCart001()).orElseThrow(), createCart002(),createCart003());
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
    @DisplayName("Add product to cart")
    void addProductWithQuantity(){
        Cart cart = new Cart(1L, 1L, Status.DRAFT,14, 0);
        Product product = new Product(1L, 3, "Producto de prueba", 0.5, 5);
        when(shoppingCartRepository.findById(any())).thenReturn(Optional.of(cart));

        when(shoppingCartRepository.save(any())).thenReturn(cart);
        Cart updatedCart = service.addProductToCartWithQuantity(1L, product, 5);

        assertNotNull(updatedCart);
        assertEquals(1L, updatedCart.getId());
        // Verificamos que se haya guardado en el repositorio
        verify(shoppingCartRepository).save(cart);
    }


}







