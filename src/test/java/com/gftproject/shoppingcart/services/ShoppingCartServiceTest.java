package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Product;
import com.gftproject.shoppingcart.model.Status;
import com.gftproject.shoppingcart.repositories.ShoppingCartRepository;
import org.junit.jupiter.api.BeforeEach;
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


class ShoppingCartServiceTest {
    @InjectMocks
    ShoppingCartServiceImpl service;

    @Mock
    ShoppingCartRepository shoppingCartRepository;

    @BeforeEach
    void setUp() {
        // Instanciar Shopping cart Service y istanciar con new mock de repository
        MockitoAnnotations.openMocks(this);
        service = new ShoppingCartServiceImpl(shoppingCartRepository);
    }

    @Test
    void getCartsByStatus() {
        //Given
        List<Cart> carts = Arrays.asList(Optional.of(createCart001()).orElseThrow(), createCart002().orElseThrow(),createCart003().orElseThrow());
        when(shoppingCartRepository.findAllByStatus(any())).thenReturn(carts);

        //when
        List<Cart> allCarts = service.findAllByStatus(Status.DRAFT);

        //then
        assertNotNull(allCarts);
        assertEquals(3, allCarts.size());
        verify(shoppingCartRepository).findAllByStatus(Status.DRAFT);
    }

    @Test
    void submitCart(){
        when(shoppingCartRepository.modifyCartStatus(any(), eq(Status.SUBMITTED))).thenReturn(createCart001());

        Cart submittedCart = service.submitCart(1L);

        assertNotNull(submittedCart);
        assertEquals(Status.SUBMITTED, submittedCart.getStatus());

        assertEquals(1L, submittedCart.getId());

    }


    @Test
    void addProductWithQuantity(){
        Cart cart = new Cart(1L, 1L, Status.DRAFT,14);
        Product product = new Product(1L, 3, "Producto de prueba", 0.5);
        when(shoppingCartRepository.findById(any())).thenReturn(Optional.of(cart));

        when(shoppingCartRepository.save(any())).thenReturn(cart);
        Cart updatedCart = service.addProductToCartWithQuantity(1L, product, 5);

        assertNotNull(updatedCart);
        assertEquals(1L, updatedCart.getId());
        // Verificamos que se haya guardado en el repositorio
        verify(shoppingCartRepository).save(cart);
    }


}







