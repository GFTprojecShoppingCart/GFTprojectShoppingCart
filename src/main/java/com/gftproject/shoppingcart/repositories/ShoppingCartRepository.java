package com.gftproject.shoppingcart.repositories;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findAllByStatus(Status status);

    @Query("SELECT c FROM Cart c JOIN c.products p WHERE KEY(p) IN :productIds")
    List<Cart> findCartsByProductIds(List<Long> productIds);


}
