package com.gftproject.shoppingcart.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.gftproject.shoppingcart.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;

@DataJpaTest
class ShoppingCartRepositoryTest {

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("GIVEN a status WHEN testFindAllByStatus THEN find all carts by status")
    void testFindAllByStatus() {
        List<Cart> cart = shoppingCartRepository.findAllByStatus(Status.DRAFT);

        assertFalse(cart.isEmpty());
        assertThat(cart).isNotEmpty().hasSize(2);
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