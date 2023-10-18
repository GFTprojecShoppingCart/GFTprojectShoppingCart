package com.gftproject.shoppingcart.controllers;

import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Product;
import com.gftproject.shoppingcart.model.Status;
import com.gftproject.shoppingcart.services.ShoppingCartService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShoppingCartController {

    ShoppingCartService service;

    public ShoppingCartController(ShoppingCartService service) {
        this.service = service;
    }

    @GetMapping("/carts/")
    public ResponseEntity<List<Cart>> findAllByStatus(@RequestParam(required = false) Status status) {
        HttpHeaders headers = new HttpHeaders();
        List<Cart> cartList;

        if (status == null) {
            cartList = service.findAll();
        } else {
            cartList = service.findAllByStatus(status);
        }
        return new ResponseEntity<>(cartList, headers, HttpStatus.OK);
    }

    @PostMapping("/carts/{userId}")
    public ResponseEntity<Cart> createCart(@PathVariable String userId) {
        HttpHeaders headers = new HttpHeaders();
        if(!StringUtils.isNumeric(userId)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(service.createCart(Long.parseLong(userId)), headers, HttpStatus.CREATED);
    }

    @PutMapping("/carts/{cartId}")
    public ResponseEntity<Cart> submitCart(@PathVariable String cartId) {
        try {
            if(!StringUtils.isNumeric(cartId)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(service.submitCart(Long.parseLong(cartId)), new HttpHeaders(), HttpStatus.OK);
        } catch (NotEnoughStockException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/carts/{cartId}/addProduct")
    public ResponseEntity<Cart> addProductToCart(
            @PathVariable Long cartId,
            @PathVariable Long productId,
            @RequestParam int quantity) {

        HttpHeaders headers = new HttpHeaders();

        Cart updatedCart = service.addProductToCartWithQuantity(cartId, productId, quantity);

        if (updatedCart == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(updatedCart, headers, HttpStatus.OK);
    }

    @PutMapping("/carts/updateStock/")
    public ResponseEntity<List<Cart>> updateProductsFromCarts(List<Product> productIds) {
        List<Cart> updatedCarts = service.updateProductsFromCarts(productIds);
        return new ResponseEntity<>(updatedCarts, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/carts/{idCart}")
    public ResponseEntity<Void> deleteShoppingCart(@PathVariable Long idCart) {
        service.deleteCart(idCart);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
