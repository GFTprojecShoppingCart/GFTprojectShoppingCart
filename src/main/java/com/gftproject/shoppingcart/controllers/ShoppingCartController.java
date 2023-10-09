package com.gftproject.shoppingcart.controllers;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;
import com.gftproject.shoppingcart.services.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
public class ShoppingCartController {

    @Autowired
    ShoppingCartService service;

    @GetMapping("/carts/")
    public ResponseEntity<Collection<Cart>> findAllByStatus(@RequestParam(required = false) Status status){
        HttpHeaders headers = new HttpHeaders();
        List<Cart> cartList;

        if (status == null){
            cartList =  service.findAll();
        }else {
            cartList = service.findAllByStatus(status);
        }

        return new ResponseEntity<>(cartList, headers, HttpStatus.OK);
    }

    @PostMapping("/carts/{id_user}")
    public ResponseEntity<Cart> createShoppingCart(@PathVariable Long id_user){
        HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<>(service.createCart(id_user), headers, HttpStatus.CREATED);
    }

    @DeleteMapping("/carts/{id_user}")
    public ResponseEntity<Cart> deleteShoppingCart(@PathVariable Long id_user) {
        HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<>(service.deleteCart(id_user), headers, HttpStatus.OK);
    }
}
