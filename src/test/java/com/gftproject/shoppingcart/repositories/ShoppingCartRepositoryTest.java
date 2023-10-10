package com.gftproject.shoppingcart.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.gftproject.shoppingcart.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;

@DataJpaTest
public class ShoppingCartRepositoryTest {

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Test
    void testFindAllByStatus() {
        List<Cart> cart = shoppingCartRepository.findAllByStatus(Status.DRAFT);

        assertFalse(cart.isEmpty());
        assertEquals(2, cart.size());

    }

    @Test
    void testAddProductWithQuantity(){

        List<Cart> cart = shoppingCartRepository.findById();
    }
    
}
