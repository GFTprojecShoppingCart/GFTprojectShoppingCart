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
    CartRepository CartRepository;


    @Test
    @DisplayName("Find all by Carts by Status")
    void testFindAllByUserId() {
        List<Cart> cart = CartRepository.findAllByUserId(1L);

        assertFalse(cart.isEmpty());
        assertEquals(2, cart.size());
        assertEquals(1, cart.get(0).getUserId());
    }


    @Test
    @DisplayName("GIVEN a status WHEN testFindAllByStatus THEN find all carts by status")
    void testFindAllByStatus() {
        List<Cart> cart = CartRepository.findAllByStatus(Status.DRAFT);

        assertFalse(cart.isEmpty());
        assertThat(cart).isNotEmpty().hasSize(2);
    }

    @Test
    @DisplayName("GIVEN cartId WHEN deleteCart THEN delete a cart object")
    void deleteCart() {
        Cart cart = CartRepository.findById(1L).orElseThrow();
        assertEquals(1L, cart.getId());

        CartRepository.delete(cart);

        assertThrows(NoSuchElementException.class, () -> {
            CartRepository.findById(1L).orElseThrow();
        });

    }

}