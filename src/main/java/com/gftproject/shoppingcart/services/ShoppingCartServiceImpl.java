package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;
import com.gftproject.shoppingcart.repositories.ShoppingCartRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Override
    public List<Cart> findAllByStatus(Status status) {
        return shoppingCartRepository.findAllByStatus(status);
    }

    @Override
    public List<Cart> findAll() {
        return null;
    }

    @Override
    public Cart createCart(Long id_user) {
        return null;
    }
}
