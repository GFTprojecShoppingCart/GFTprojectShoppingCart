package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
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
    private final CartComputationsService computationsService;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, CartComputationsService computationsService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.computationsService = computationsService;
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
        cart.setUserId(userId);
        return shoppingCartRepository.save(cart);
    }

    //TODO
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

        try {
            boolean stock = computationsService.checkStock(cart.getProducts());
            // TODO Validate User

            // TODO Compute price -> Cosas

            computationsService.computeFinalValues(cart);
        } catch (NotEnoughStockException e) {
            throw new RuntimeException(e);
        }

        // TODO Check TAX / Payment / weight

        // TODO Check cart validity
        // TODO Change status AND send to almacen

        cart.setStatus(Status.SUBMITTED);

        return shoppingCartRepository.save(cart);
    }

    public void addProductWithQuantity(Cart cart, Product product, int quantity) {

        Map<Product, Integer> products = cart.getProducts();
        if (cart.getProducts().containsKey(product)) {
            if(product.getStorageQuantity()>= quantity){
                int currentQuantity = products.get(product);
                int newQuantity = currentQuantity + quantity;
                products.put(product, currentQuantity + newQuantity);
            }
        } else {
            products.put(product, quantity);
        }
    }

    public Object updateStockCart(Object any) {
        return null;
    }
}
