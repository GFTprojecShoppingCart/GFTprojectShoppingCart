package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Product;
import com.gftproject.shoppingcart.model.Status;
import com.gftproject.shoppingcart.repositories.ShoppingCartRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    public void addProductWithQuantity(Cart cart, Product product, int quantity) {
        if (cart.getProducts().containsKey(product)) {
            int currentQuantity = products.get(product);
            products.put(product, currentQuantity + quantity);
        } else {
            products.put(product, quantity);
        }
    }
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

    @Override
    public Cart addProductToCartWithQuantity(Long cartId, Product product, int quantity){
        Optional<Cart> optionalCart = shoppingCartRepository.findById(cartId);

        if( optionalCart.isPresent()){
            Cart cart = optionalCart.get();
            cart.addProductWithQuantity(product, quantity);
            return shoppingCartRepository.save(cart);
        }else{
            Cart newCart = new Cart();
            newCart.setStatus(Status.DRAFT);
            return shoppingCartRepository.save(newCart);
        }
    }

}
