package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Product;
import com.gftproject.shoppingcart.model.Status;
import com.gftproject.shoppingcart.repositories.ShoppingCartRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository) {
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
    public Cart addProductToCartWithQuantity(Long cartId, Product product, int quantity) {
        Optional<Cart> optionalCart = shoppingCartRepository.findById(cartId);

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();

            addProductWithQuantity(cart, product, quantity);
            return shoppingCartRepository.save(cart);
        } else {
            Cart newCart = new Cart();
            newCart.setStatus(Status.DRAFT);
            return shoppingCartRepository.save(newCart);
        }
    }

    @Override
    public void deleteCart(Long idCart) {
        shoppingCartRepository.deleteById(idCart);
    }

    @Override
    public Cart submitCart(Long idCart) {

        Cart cart = shoppingCartRepository.findById(idCart).orElseThrow();

        boolean stock = checkStock(cart);

        // TODO Validate User

        // TODO Compute price -> Cosas

        // TODO Check stock
        // TODO Change status

        cart.setStatus(Status.SUBMITTED);

        return shoppingCartRepository.save(cart);
    }

    public void addProductWithQuantity(Cart cart, Product product, int quantity) {
        Map<Product, Integer> products = cart.getProducts();
        if (cart.getProducts().containsKey(product)) {
            int currentQuantity = products.get(product);
            products.put(product, currentQuantity + quantity);
        } else {
            products.put(product, quantity);
        }
    }

    @Override
    public double computePrice(Cart cart) {
        return 3.4;
    }

    public boolean checkStock(Cart cart) {
        return true;
    }

}
