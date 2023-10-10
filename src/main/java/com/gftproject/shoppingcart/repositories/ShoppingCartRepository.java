package com.gftproject.shoppingcart.repositories;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatusCode;

public interface ShoppingCartRepository extends JpaRepository<Cart, Long > {

    List<Cart> findAllByStatus(Status status);

    @Modifying
    @Query("update Cart c set c.status = ?2 where c.id = ?1")
    Cart submitCart(Long idCart, Status status);
}
