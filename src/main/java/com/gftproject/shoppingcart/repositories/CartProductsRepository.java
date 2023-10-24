package com.gftproject.shoppingcart.repositories;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartProductsRepository extends JpaRepository<CartProduct, Long> {

    List<CartProduct> findAllByCartId(long cartId);

    List<CartProduct> findByProductIn(List<Long> productIds);

    @Query("SELECT DISTINCT cp.cart FROM CartProduct cp WHERE cp.product IN :productIds")
    List<Cart> findCartsByProductIds(@Param("productIds") List<Long> productIds);


    CartProduct findByCartAndProduct(Cart cart, Long product);
}
