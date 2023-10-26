package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.CartNotFoundException;
import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.exceptions.UserNotFoundException;
import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.ProductDTO;
import com.gftproject.shoppingcart.model.Status;

import java.util.List;

public interface ShoppingCartService {

    List<Cart> findAllByStatus(Status status);

    List<Cart> findAll();

    List<Cart> findAllByUserId(Long userId);

    Cart createCart(Long userId) throws UserNotFoundException;

    Cart addProductToCartWithQuantity(long cartId, long productId, int quantity) throws ProductNotFoundException, NotEnoughStockException, CartNotFoundException;

    void deleteCart(Long idCart);

    Cart submitCart(Long idCart) throws NotEnoughStockException, ProductNotFoundException, UserNotFoundException;

    void updateProductsFromCarts(List<ProductDTO> products);

    void deleteProductFromCart(Long cartId, Long productId);
}