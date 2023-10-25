package com.gftproject.shoppingcart.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;

@DataJpaTest
class CartRepositoryTest {

    @Autowired
    CartRepository shoppingCartRepository;


    @Test
    @DisplayName("Find all by Carts by Status")
    void testFindAllByUserId() {
        List<Cart> cart = shoppingCartRepository.findAllByUserId(1L);

        assertFalse(cart.isEmpty());
        assertEquals(2, cart.size());
        assertEquals(1, cart.get(0).getUserId());
    }


    @Test
    @DisplayName("GIVEN a status WHEN testFindAllByStatus THEN find all carts by status")
    void testFindAllByStatus() {
        List<Cart> cart = shoppingCartRepository.findAllByStatus(Status.DRAFT);

        assertFalse(cart.isEmpty());
        assertThat(cart).isNotEmpty().hasSize(1);
    }

    @Test
    @DisplayName("GIVEN cartId WHEN deleteCart THEN delete a cart object")
    void deleteCart() {
        Cart cart = shoppingCartRepository.findById(1L).orElseThrow();
        assertEquals(1L, cart.getId());

        shoppingCartRepository.delete(cart);

        assertThrows(NoSuchElementException.class, () -> {
            shoppingCartRepository.findById(1L).orElseThrow();
        });

    }

}