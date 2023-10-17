package com.gftproject.shoppingcart.controllers;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Product;
import com.gftproject.shoppingcart.model.Status;
import com.gftproject.shoppingcart.services.ShoppingCartService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
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

    @PostMapping("/carts/{id_user}")
    public ResponseEntity<Cart> createShoppingCart(@PathVariable String userId) {
        HttpHeaders headers = new HttpHeaders();
        if(StringUtils.isNumeric(userId)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long id_user = Long.parseLong(userId);

        return new ResponseEntity<>(service.createCart(id_user), headers, HttpStatus.CREATED);
    }

    @PutMapping("/carts/{cartId}")
    public ResponseEntity<Cart> submitCart(@PathVariable String cartId) {
        HttpHeaders headers = new HttpHeaders();
        if(!StringUtils.isNumeric(cartId)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long id_cart = Long.parseLong(cartId);
        return new ResponseEntity<>(service.createCart(id_cart), headers, HttpStatus.CREATED);

    }

    @PutMapping("/carts1/{cartId}")
    public ResponseEntity<Cart> addProductWithQuantity(@PathVariable Long cartId) {
        //TODO change carts1
        HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<>(service.submitCart(cartId), headers, HttpStatus.OK);
    }

    @PutMapping("/updateProducts")
    public ResponseEntity<List<Long>> updateProductsFromCarts(List<Product> productList){

        service.updateProductsFromCarts(productList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/carts/{idCart}")
    public ResponseEntity<Void> deleteShoppingCart(@PathVariable Long idCart) {
        service.deleteCart(idCart);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<Cart> updateStockCart(long l) {
        return null;
    }
}
