package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.model.Product;

import java.util.List;

public interface ProductService {

    public Product getProductById(Long productId);

    public List<Product> getProductsByIds(List<Long> productIds);
}
