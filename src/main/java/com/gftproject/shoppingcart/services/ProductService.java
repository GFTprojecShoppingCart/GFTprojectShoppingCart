package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.model.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {

    Product getProductById(Long productId) throws ProductNotFoundException;

    List<Product> getProductsByIds(List<Long> productIds) throws ProductNotFoundException;

    List<Product> getProductsToSubmit(List<Product> productList) throws ProductNotFoundException, NotEnoughStockException;
}
