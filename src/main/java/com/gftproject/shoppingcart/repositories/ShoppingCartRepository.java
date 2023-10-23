package com.gftproject.shoppingcart.repositories;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findAllByStatus(Status status);

    @Query("SELECT c FROM Cart c JOIN c.productList p WHERE KEY(p) IN :productIds")
    List<Cart> findCartsByProductIds(List<Long> productIds);

    List<Cart> findAllByUserId(Long userId);
}
