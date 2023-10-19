package com.gftproject.shoppingcart.repositories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.gftproject.shoppingcart.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;

@DataJpaTest
public class ShoppingCartRepositoryTest {

    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartRepository mockRepository;

    @BeforeEach
    void setUp(){
        shoppingCartRepository = mockRepository;
    }

    @Test
    @DisplayName("GIVEN a status WHEN testFindAllByStatus THEN find all carts by status")
    void testFindAllByStatus() {
        List<Cart> cartList = List.of(new Cart(), new Cart());
        when(mockRepository.findAllByStatus(Status.DRAFT)).thenReturn(cartList);

        List<Cart> cart = shoppingCartRepository.findAllByStatus(Status.DRAFT);

        assertFalse(cart.isEmpty());
        assertEquals(2, cart.size());

    }

    @Test
    @DisplayName("GIVEN cartId WHEN deleteCart THEN delete a cart object")
    void deleteCart() {
        Cart cart = new Cart();
        when(mockRepository.findById(1L)).thenReturn(Optional.of(cart));

        shoppingCartRepository.delete(cart);

        when(mockRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            shoppingCartRepository.findById(1L).orElseThrow();
        });

    }

}
