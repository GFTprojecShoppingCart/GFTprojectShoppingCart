package com.gftproject.shoppingcart.repositories;

import com.gftproject.shoppingcart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<Cart, Long > {
}
