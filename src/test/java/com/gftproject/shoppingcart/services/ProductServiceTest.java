package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.ProductData;
import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductServiceImpl(restTemplate);
        product = ProductData.createProduct001();
    }

    @Test
    @DisplayName("GIVEN a productId WHEN a specific product is searched THEN it will be returned as a POJO")
    void testGetProductById() throws ProductNotFoundException {
        Long productId = 123L;

        // Configurar el comportamiento del restTemplate mock
        ResponseEntity<Product> mockResponseEntity = new ResponseEntity<>(product, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Product.class))).thenReturn(mockResponseEntity);

        // Llamar al método de servicio
        Product result = productService.getProductById(productId);

        // Realizar aserciones utilizando AssertJ
        assertThat(result).isNotNull().isEqualToComparingFieldByField(product);
    }
    @Test
    @DisplayName("GIVEN a list of products IDs WHEN a request is sent to the microservice THEN a list of products is returned")
    void getProductsByIds() throws ProductNotFoundException {
        List<Product> productList = productService.getProductsByIds(new ArrayList<>());
        assertThat(productList.get(0).getId()).isEqualTo(2L);

    }

    @Test
    @DisplayName("GIVEN a list of products and amounts ready for purchase WHEN is sent to the microservice THEN checks if the purchase is completed")
    void testGetProductsToSubmit() throws ProductNotFoundException, NotEnoughStockException {
        // Datos de entrada
        Map<Long, Integer> productsToSubmit = new HashMap<>();
        productsToSubmit.put(1L, 5);
        productsToSubmit.put(2L, 3);

        // Respuesta simulada
        List<Product> mockResponse = new ArrayList<>();

        // Configurar el comportamiento del restTemplate mock
        ResponseEntity<List<Product>> mockResponseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), any(ParameterizedTypeReference.class)).thenReturn(mockResponseEntity);

        // Llamar al método de servicio
        List<Product> result = productService.getProductsToSubmit(productsToSubmit);

        // Realizar aserciones utilizando AssertJ
        assertThat(result).isNotNull().isEqualTo(mockResponse);
    }
}
