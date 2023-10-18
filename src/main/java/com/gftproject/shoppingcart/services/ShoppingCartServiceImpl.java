package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Product;
import com.gftproject.shoppingcart.model.Status;
import com.gftproject.shoppingcart.repositories.ShoppingCartRepository;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

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
    public List<Cart> findAll() {
        return shoppingCartRepository.findAll();
    }

    @Override
    public List<Cart> findAllByStatus(Status status) {
        return shoppingCartRepository.findAllByStatus(status);
    }

    @Override
    public Cart createCart(Long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        return shoppingCartRepository.save(cart);
    }

    @Override
    public Cart addProductToCartWithQuantity(Long cartId, Long productId, int quantity) {
        Optional<Cart> optionalCart = shoppingCartRepository.findById(cartId);

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();

            Product product = productService.getProductById(productId);

            addProductWithQuantity(cart, product, quantity);
            return shoppingCartRepository.save(cart);
        } else {
            Cart newCart = new Cart();
            newCart.setStatus(Status.DRAFT);
            return shoppingCartRepository.save(newCart);
        }
    }


    @Override
    public Cart submitCart(Long idCart) throws NotEnoughStockException {
        // Obtenemos el carrito
        Cart cart = shoppingCartRepository.findById(idCart).orElseThrow();
    
        // Obtener IDs de los productos en el carrito
        List<Long> productIds = new ArrayList<>(cart.getProducts().keySet());
    

        // Obtener los productos del almacén
        List<Product> warehouseStock = productService.getProductsByIds(productIds);
        // Comprobar el stock
        List<Long> productsWithoutStock = null;
        try {
            productsWithoutStock = computationsService.checkStock(cart.getProducts(), warehouseStock);
        } catch (ProductNotFoundException e) {
            System.out.println("oof");
        }
        // TODO: Validar al usuario

        userService.validate(cart.getUserId());

        if (productsWithoutStock.isEmpty()) {
            // Realizar cálculos de precio
            Pair<BigDecimal, BigDecimal> pair = null;
            try {
                pair = computationsService.computeFinalValues(cart.getProducts(), warehouseStock);
            } catch (ProductNotFoundException e) {
                System.out.println("oof");
            }

            // Cambiar el estado del carrito
            cart.setFinalWeight(pair.a);
            cart.setFinalPrice(pair.b);
            cart.setStatus(Status.SUBMITTED);

            // Guardar el carrito
            return shoppingCartRepository.save(cart);
        } else {
            // No hay suficiente stock, devuelve el carrito en estado DRAFT
            throw new NotEnoughStockException(productsWithoutStock);
        }   

        
    }

    public List<Cart> updateProductsFromCarts(List<Product> productList) {

        List<Long> productsIds = productList.stream().map(Product::getId).toList();
        List<Cart> shoppingCarts = shoppingCartRepository.findCartsByProductIds(productsIds);

        try {

            for (Cart cart : shoppingCarts) {
                cart.setInvalidProducts(computationsService.checkStock(cart.getProducts(), productList));
                shoppingCartRepository.save(cart);
            }

        } catch (ProductNotFoundException e) {
            return new ArrayList<>();
        }
        return shoppingCarts;
    }

    public void addProductWithQuantity(Cart cart, Product product, int quantity) {

        Map<Long, Integer> products = cart.getProducts();
        if (cart.getProducts().containsKey(product.getId())) {
            if (product.getStorageQuantity() >= quantity) {
                int currentQuantity = products.get(product.getId());
                int newQuantity = currentQuantity + quantity;
                products.put(product.getId(), currentQuantity + newQuantity);
            }
        } else {
            products.put(product.getId(), quantity);
        }
    }

    @Override
    public void deleteCart(Long cartId) {
        shoppingCartRepository.deleteById(cartId);
    }

}
