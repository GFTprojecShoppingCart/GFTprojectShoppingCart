package com.gftproject.shoppingcart.repositories;

import com.gftproject.shoppingcart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
