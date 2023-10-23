package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.model.Product;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartComputationsService {

    public List<Long> checkStock(Map<Long, Integer> cartProducts, List<Product> updatedProducts) {

        List<Long> productsWithoutStock = new ArrayList<>();

        for (Product product : updatedProducts) {
            if (cartProducts.containsKey(product.getId())) {
                int productInCart = cartProducts.get(product.getId());
                if (product.getStorageQuantity() < productInCart) {
                    productsWithoutStock.add(product.getId());
                }
            }
        }
        return productsWithoutStock;
    }

    public Pair<BigDecimal, BigDecimal> computeFinalValues(Map<Long, Integer> cartProducts, List<Product> warehouseStock) {

        BigDecimal totalWeight = new BigDecimal(0);
        BigDecimal totalPrice = new BigDecimal(0);

        for (Product product : warehouseStock) {

            int quantity = cartProducts.get(product.getId());
            totalWeight = totalWeight.add(product.getWeight().multiply(BigDecimal.valueOf(quantity)));
            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

        }
        return new Pair<>(totalWeight, totalPrice);
    }

    public double computeByWeight(double cartWeight) {
        double weightCost;
        if (cartWeight <= 5) {
            weightCost = 5;
        } else if (cartWeight <= 10) {
            weightCost = 10;
        } else if (cartWeight <= 20) {
            weightCost = 20;
        } else {
            weightCost = 50;
        }
        return weightCost;
    }

}
