package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Product;
import com.gftproject.shoppingcart.model.Status;
import com.gftproject.shoppingcart.repositories.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartComputationsService computationsService;
    private final ProductServiceImpl productService;
    private final UserServiceImpl userService;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, CartComputationsService computationsService, ProductServiceImpl productService, UserServiceImpl userService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.computationsService = computationsService;
        this.productService = productService;
        this.userService = userService;
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
    public Cart submitCart(Long idCart) {
        // We obtain the cart
        Cart cart = shoppingCartRepository.findById(idCart).orElseThrow();
        List<Long> productIds = new ArrayList<>(cart.getProducts().keySet());

        try {
            // We obtain the cart products from their endpoint
            List<Product> warehouseStock = productService.getProductsByIds(productIds);
            List<Long> productsWithoutStock = computationsService.checkStock(cart.getProducts(), warehouseStock);
            // TODO Validate User

            userService.validate();

            // TODO Compute price -> Cosas

            if (productsWithoutStock.isEmpty()) {

                // TODO Check TAX / Payment / weight

                // TODO Check cart validity
                // TODO Change status AND send to almacen

                computationsService.computeFinalValues(cart);

                cart.setStatus(Status.SUBMITTED);

                return shoppingCartRepository.save(cart);
            }

        } catch (NotEnoughStockException e) {
            throw new RuntimeException(e);
        }
        return cart;
    }

    public List<Cart> updateProductsFromCarts(List<Product> productList) {
        List<Long> productsIds = productList.stream().map(Product::getId).toList();
        List<Cart> shoppingCarts = shoppingCartRepository.findCartsByProductIds(productsIds);

        for (Cart cart : shoppingCarts) {
            cart.setInvalidProducts(computationsService.checkStock(cart.getProducts(), productList));
        }
        return shoppingCarts;

    }

    public void addProductWithQuantity(Cart cart, Product product, int quantity) {

        Map<Long, Integer> products = cart.getProducts();
        if (cart.getProducts().containsKey(product)) {
            if (product.getStorageQuantity() >= quantity) {
                int currentQuantity = products.get(product);
                int newQuantity = currentQuantity + quantity;
                products.put(product.getId(), currentQuantity + newQuantity);
            }
        } else {
            products.put(product.getId(), quantity);
        }
    }

    public Object updateStockCart(Object any) {
        return null;
    }

    @Override
    @Transactional
    public void deleteCart(Long cartId) {shoppingCartRepository.deleteById(cartId);}

}
