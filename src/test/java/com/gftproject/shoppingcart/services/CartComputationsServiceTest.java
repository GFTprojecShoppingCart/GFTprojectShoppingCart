package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.CartsData;
import com.gftproject.shoppingcart.ProductData;
import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.CartProduct;
import com.gftproject.shoppingcart.model.ProductDTO;
import com.gftproject.shoppingcart.model.Status;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CartComputationsServiceTest {

    private Cart cart;
    private CartComputationsService computationsService;

    @BeforeEach
    void setUp() {
        computationsService = new CartComputationsService();
        cart = CartsData.createCart001();
    }

    @Test
    @DisplayName("GIVEN a shopping cart and a Product list WHEN it scans if there's enough stock for the products in cart THEN will return the elements without enough stock")
    void checkStock() {
        Cart cart = new Cart(1L, Status.SUBMITTED, new BigDecimal("100.00"), new BigDecimal("5.0"));

        List<CartProduct> cartProductList = new ArrayList<>();
        CartProduct cartProduct = new CartProduct(cart, 1L, true, 5);
        cartProductList.add(cartProduct);


        List<ProductDTO> productDTOList = new ArrayList<>();
        ProductDTO productDTO = new ProductDTO(1L, new BigDecimal("50.00"), 10, new BigDecimal("10.00"));
        productDTOList.add(productDTO);
        List<Long> productsWithoutStock = computationsService.getProductIdsWithoutStock(cartProductList, productDTOList);
        assertThat(productsWithoutStock.contains(5L));
    }

    @Test
    @DisplayName("GIVEN a shopping cart and a Product list WHEN method is called THEN returns the final price and weight of all products in cart ")
    void computeFinalValues() {

        Pair<BigDecimal, BigDecimal> pairWeightValue = computationsService.computeFinalValues(cart.getProductList(), ProductData.getWarehouseStock());

        assertThat(new BigDecimal("120.4")).isEqualTo(pairWeightValue.a);
        assertThat(new BigDecimal("287.86")).isEqualTo(pairWeightValue.b);
    }
}