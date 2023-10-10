package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Product;
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
        return shoppingCartRepository.save(new Cart());
    }

    @Override
    public Cart addProduct(Long idUser, Long idCart, Product product) {
        return null;
    }

    @Override
    public Cart deleteCart(Long idCart) {
        return null;
    }

    @Override
    public Cart submitCart(Long idCart) {
        return shoppingCartRepository.submitCart(idCart, Status.SUBMITTED);
    }

}
