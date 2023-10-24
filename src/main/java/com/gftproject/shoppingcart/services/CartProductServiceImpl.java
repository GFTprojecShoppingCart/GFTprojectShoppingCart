package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.model.CartProduct;
import com.gftproject.shoppingcart.repositories.CartProductsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartProductServiceImpl {

    private final CartProductsRepository repository;

    public CartProductServiceImpl(CartProductsRepository repository) {
        this.repository = repository;
    }

    List<CartProduct> getAllCartProductsFromCart(Long cartId){
        return repository.findAll();
    }

}
