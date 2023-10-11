package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Product;

import java.util.Map;

public class CartComputationsService {

    public double computePrice(Cart cart) {
        return 3.4;
    }

    public boolean checkStock(Cart cart) {
        return true;
    }

    public void computeFinalValues(Cart cart){

        double totalWeight = 0.0;
        double totalPrice = 0.0;

        for (Map.Entry<Product, Integer> entry : cart.getProducts().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            double productWeight = product.getWeight();
            double productPrice = product.getPrice();

            totalWeight += productWeight * quantity;
            totalPrice += productPrice * quantity;
        }

        cart.setFinalWeight(totalWeight);
        cart.setFinalPrice(totalPrice);
    }

    public boolean cartValidity(Cart cart){
        //TODO recorre todos los productos, y comprueba que hay suficiente stock
        return false;
    }

}
