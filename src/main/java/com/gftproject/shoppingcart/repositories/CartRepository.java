package com.gftproject.shoppingcart.repositories;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findAllByStatus(Status status);

    @Query("SELECT c FROM Cart c JOIN c.cartProducts p WHERE KEY(p) IN :product")
    List<Cart> findCartsByProduct(List<Long> product);

    @Query("SELECT DISTINCT c FROM Cart c " +
            "JOIN c.cartProducts cp " +
            "WHERE cp.product IN :productIds")
    List<Cart> findCartsByProductIds(@Param("productIds") List<Long> productIds);


    List<Cart> findAllByUserId(Long userId);
}
