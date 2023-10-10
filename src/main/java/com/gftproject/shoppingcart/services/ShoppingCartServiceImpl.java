package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Product;
import com.gftproject.shoppingcart.model.Status;
import com.gftproject.shoppingcart.repositories.ShoppingCartRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository){
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public List<Cart> findAllByStatus(Status status) {
        return shoppingCartRepository.findAllByStatus(status);
    }

    @Override
    public List<Cart> findAll() {
        return shoppingCartRepository.findAll();
    }

    @Override
    public Cart createCart(Long userId) {
        Cart cart = new Cart();
        cart.setUser_id(userId);
        return shoppingCartRepository.save(cart);
    }

    @Override
    public Cart addProduct(Long idUser, Long idCart, Product product) {
        return null;
    }

    @Override
    public void deleteCart(Long idCart) {
        shoppingCartRepository.deleteById(idCart);
    }

    @Override
    public Cart submitCart(Long idCart) {
        return shoppingCartRepository.modifyCartStatus(idCart, Status.SUBMITTED);
    }

}
