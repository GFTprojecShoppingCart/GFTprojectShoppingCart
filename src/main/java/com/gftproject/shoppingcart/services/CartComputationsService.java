package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class CartComputationsService {

    public boolean checkStock(Map<Product, Integer> products) {
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            int quantity = entry.getValue();

            if (quantity > entry.getKey().getStorageQuantity()){
                return false;
            }
        }
        return true;
    }

    public void computeFinalValues(Cart cart) throws NotEnoughStockException {

        BigDecimal totalWeight = new BigDecimal(0);
        BigDecimal totalPrice = new BigDecimal(0);

        for (Map.Entry<Product, Integer> entry : cart.getProducts().entrySet()) {

            Product product = entry.getKey();
            int quantity = entry.getValue();

            if (quantity > entry.getKey().getStorageQuantity()){
                throw new NotEnoughStockException();
            }

            BigDecimal productWeight = product.getWeight();
            BigDecimal productPrice = product.getPrice();

            totalWeight = totalWeight.add(productWeight.multiply(BigDecimal.valueOf(quantity)));
            totalPrice = totalPrice.add(productPrice.multiply(BigDecimal.valueOf(quantity)));

        }

        cart.setFinalWeight(totalWeight);
        cart.setFinalPrice(totalPrice);
    }

    public boolean cartValidity(Cart cart){
        //TODO recorre todos los productos, y comprueba que hay suficiente stock
        return false;
    }

}
