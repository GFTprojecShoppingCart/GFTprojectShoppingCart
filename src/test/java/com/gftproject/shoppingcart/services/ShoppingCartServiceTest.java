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
        List<Cart> carts = Arrays.asList(createCart001().orElseThrow(), createCart002().orElseThrow(),createCart003().orElseThrow());
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
        when(shoppingCartRepository.modifyCartStatus(any(), eq(Status.SUBMITTED))).thenReturn(createSampleCart());

        Cart submittedCart = service.submitCart(1L);

        assertNotNull(submittedCart);
        assertEquals(1L, submittedCart.getId());

    }


    @Test
    void addProductWithQuantity(){
        Cart cart = new Cart(1L, 1L, Status.DRAFT);
        Product product = new Product("Producto de prueba", "Descripción de prueba", "Categoría de prueba", 10.0, 0.5, 100);
        when(shoppingCartRepository.findById(any())).thenReturn(Optional.of(cart));

        when(shoppingCartRepository.save(any())).thenReturn(cart);
        Cart updatedCart = service.addProductToCartWithQuantity(1L, product, 5);

        assertNotNull(updatedCart);
        assertEquals(1L, updatedCart.getId());
        verify(shoppingCartRepository).save(cart);
    }



    //TODO

    @Test
    void deleteCart(){
        Cart cart = new Cart();

        when(shoppingCartRepository.deleteById(any())).thenReturn(createSampleCart());

        Cart submittedCart = service.submitCart(1L);

        assertNotNull(submittedCart);
        assertEquals(1L, submittedCart.getId());
;
    }
}






