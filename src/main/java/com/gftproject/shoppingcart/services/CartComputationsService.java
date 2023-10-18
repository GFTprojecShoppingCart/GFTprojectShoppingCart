package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Product;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class CartComputationsService {

    public List<Long> checkStock(Map<Long, Integer> cartProducts, List<Product> warehouseStock) throws ProductNotFoundException {

        List<Long> productsWithoutStock = new ArrayList<>();

        for (Product product : warehouseStock) {
            if (cartProducts.containsKey(product.getId())) {
                int productInCart = cartProducts.get(product.getId());
                if (product.getStorageQuantity() < productInCart) {
                    productsWithoutStock.add(product.getId());
                }
            }else {
                throw new ProductNotFoundException();
            }
        }
        return productsWithoutStock;
    }

    public Pair<BigDecimal, BigDecimal> computeFinalValues(Map<Long, Integer> cartProducts, List<Product> warehouseStock) throws ProductNotFoundException {

        BigDecimal totalWeight = new BigDecimal(0);
        BigDecimal totalPrice = new BigDecimal(0);

        for (Product product : warehouseStock) {
            if (cartProducts.containsKey(product.getId())) {
                int quantity = cartProducts.get(product.getId());
                totalWeight = totalWeight.add(product.getWeight().multiply(BigDecimal.valueOf(quantity)));
                totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
            }else {
                throw new ProductNotFoundException();
            }
        }
        return new Pair<>(totalWeight, totalPrice);
    }

}
