package com.gftproject.shoppingcart.repositories;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.CartProduct;
import com.gftproject.shoppingcart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartProductsRepository extends JpaRepository<CartProduct, Long> {

    List<CartProduct> findAllByCartId(long cartId);

    CartProduct findByCartAndProduct(Cart cart, Product product);
}
