package com.gftproject.shoppingcart.repositories;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<Cart, Long > {

    List<Cart> findAllByStatus(Status status);
}
