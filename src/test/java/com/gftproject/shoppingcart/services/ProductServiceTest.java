package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.ProductData;
import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.CartProduct;
import com.gftproject.shoppingcart.model.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    ProductServiceImpl productService;

    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductServiceImpl(restTemplate);
        productDTO = ProductData.createProductDTO001();
    }

    @Test
    @DisplayName("GIVEN a productId WHEN a specific product is searched THEN it will be returned as a POJO")
    void testGetProductById() throws ProductNotFoundException {
        Long productId = 123L;

        // Configurar el comportamiento del restTemplate mock
        ResponseEntity<ProductDTO> mockResponseEntity = new ResponseEntity<>(productDTO, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(ProductDTO.class))).thenReturn(mockResponseEntity);

        // Llamar al método de servicio
        ProductDTO result = productService.getProductById(productId);

        // Realizar aserciones utilizando AssertJ
        assertThat(result).isNotNull().isEqualToComparingFieldByField(productDTO);
    }


    @Test
    @DisplayName("GIVEN a list of products and amounts ready for purchase WHEN is sent to the microservice THEN checks if the purchase is completed")
    void testSubmitPurchase() throws ProductNotFoundException, NotEnoughStockException {
        // Datos de entrada
        List<CartProduct> productList = new ArrayList<>();
        productList.add(new CartProduct(new Cart(), 1L, true, 5));
        productList.add(new CartProduct(new Cart(), 2L, true, 3));

        // Respuesta simulada
        List<ProductDTO> mockResponse = new ArrayList<>();
        mockResponse.add(new ProductDTO(1L, new BigDecimal(25), 3, new BigDecimal(2)));
        mockResponse.add(new ProductDTO(2L, new BigDecimal(28), 34, new BigDecimal(0.5)));

        // Configurar el comportamiento del restTemplate mock
        ResponseEntity<List<ProductDTO>> mockResponseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), any(ParameterizedTypeReference.class))).thenReturn(mockResponseEntity);

        // Llamar al método de servicio
        List<ProductDTO> result = productService.submitPurchase(productList);

        // Realizar aserciones utilizando AssertJ
        assertThat(result).isNotNull().isEqualTo(mockResponse);
        // Verificar que el ID del resultado coincida con los ID de los productos en productList
        assertThat(result).extracting(ProductDTO::getId).containsExactlyElementsOf(productList.stream().map(CartProduct::getProduct).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("GIVEN a list of products WHEN is sent to the microservice THEN throws error if there's not enough stock")
    void test_submit_purchase_not_enough_stock() {
        List<CartProduct> productList = new ArrayList<>();
        productList.add(new CartProduct(new Cart(), 1L, true, 5));

        ResponseEntity<List<ProductDTO>> mockResponseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), any(ParameterizedTypeReference.class))).thenReturn(mockResponseEntity);

        assertThrows(NotEnoughStockException.class, () -> productService.submitPurchase(productList));
    }

    @Test
    @DisplayName("GIVEN a list of products WHEN is sent to the microservice THEN throws error if there's not enough stock")
    void submitPurchaseProductNotFound() {
        List<CartProduct> productList = new ArrayList<>();
        productList.add(new CartProduct(new Cart(), 1L, true, 5));

        ResponseEntity<List<ProductDTO>> mockResponseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), any(ParameterizedTypeReference.class))).thenReturn(mockResponseEntity);

        assertThrows(ProductNotFoundException.class, () -> productService.submitPurchase(productList));
    }

    @Test
    @DisplayName("GIVEN a non existing productId WHEN is sent to the microservice THEN throws error")
    void getProductByIdNotFound() {
        // Arrange
        Long productId = 123L;
        ResponseEntity<ProductDTO> mockResponseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(ProductDTO.class))).thenReturn(mockResponseEntity);

        // Act and Assert
        assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductById(productId);
        });
    }
}
