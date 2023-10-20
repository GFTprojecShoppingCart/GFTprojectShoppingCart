package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceTest {

    ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl();
    }

    @Test
    @DisplayName("GIVEN a productId WHEN a specific product is searched THEN it will be returned as a POJO")
    void getProductById() throws ProductNotFoundException {
        Product product = productService.getProductById(1L);

        assertThat(product.getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("GIVEN a list of products IDs WHEN a request is sent to the microservice THEN a list of products is returned")
    void getProductsByIds() throws ProductNotFoundException {
        List<Product> productList = productService.getProductsByIds(new ArrayList<>());
        assertThat(productList.get(0).getId()).isEqualTo(2L);

    }

    @Test
    @DisplayName("GIVEN a list of products and amounts ready for purchase WHEN is sent to the microservice THEN checks if the purchase is completed")
    void getProductsToSubmit() throws NotEnoughStockException, ProductNotFoundException {
        List<Product> product = productService.getProductsToSubmit(new HashMap<>());
        assertThat(product.get(0).getId()).isEqualTo(2L);


    }
}
