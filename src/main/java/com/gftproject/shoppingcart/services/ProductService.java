package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.model.CartProduct;
import com.gftproject.shoppingcart.model.Product;
import com.gftproject.shoppingcart.model.ProductDTO;

import java.util.List;

public interface ProductService {

    ProductDTO getProductById(Long productId) throws ProductNotFoundException;

//    List<ProductDTO> getProductsByIds(List<Long> productIds) throws ProductNotFoundException;

    List<ProductDTO> submitPurchase(List<CartProduct> productList) throws ProductNotFoundException, NotEnoughStockException;
}
