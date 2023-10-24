package com.gftproject.shoppingcart;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.CartProduct;
import com.gftproject.shoppingcart.model.ProductDTO;
import com.gftproject.shoppingcart.model.Status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductData {

    static Cart cart = new Cart(1L, 1L, new ArrayList<>(), Status.DRAFT, BigDecimal.ZERO, BigDecimal.ZERO);

    public static ProductDTO createProductDTO001() {
        return new ProductDTO(1L, new BigDecimal("25.99"), 3, new BigDecimal("55"));
    }

    public static ProductDTO createProductDTO002() {
        return new ProductDTO(2L, new BigDecimal("19.99"), 4, new BigDecimal("1.2"));
    }

    public static ProductDTO createProductDTO003() {
        return new ProductDTO(3L, new BigDecimal("12.5"), 5, new BigDecimal("0.7"));
    }

    public static ProductDTO createProductDTO004() {
        return new ProductDTO(4L, new BigDecimal("7.99"), 6, new BigDecimal("0.5"));
    }

    public static ProductDTO createProductDTO005() {
        return new ProductDTO(5L, new BigDecimal("14.99"), 1, new BigDecimal("0.3"));
    }

    public static CartProduct createCartProduct001() {
        CartProduct cartProduct = new CartProduct(cart, 1L, true, 5);
        cart.getCartProducts().add(cartProduct);
        cartProduct.setCart(cart);
        return cartProduct;
    }


    public static List<ProductDTO> getWarehouseStock() {

        List<ProductDTO> products = new ArrayList<>();

        products.add(createProductDTO001());
        products.add(createProductDTO002());
        products.add(createProductDTO003());
        products.add(createProductDTO004());
        products.add(createProductDTO005());

        return products;
    }
}