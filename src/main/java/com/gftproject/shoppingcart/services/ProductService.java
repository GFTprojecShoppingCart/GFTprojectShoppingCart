package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.model.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {

    public Product getProductById(Long productId);

    public List<Product> getProductsByIds(List<Long> productIds);

    public List<Product> getProductsToSubmit(Map<Long, Integer> product) throws ProductNotFoundException;
}
