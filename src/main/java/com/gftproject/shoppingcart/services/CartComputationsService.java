package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.model.CartProduct;
import com.gftproject.shoppingcart.model.Product;
import com.gftproject.shoppingcart.model.ProductDTO;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CartComputationsService {

    public List<Long> getProductIdsWithoutStock(List<CartProduct> productList) {
        List<Long> productsWithoutStock = new ArrayList<>();
        for (CartProduct cartProduct : productList) {
            if (cartProduct.getQuantity() > cartProduct.getProduct().getStorageQuantity()) {
                productsWithoutStock.add(cartProduct.getProduct().getId());
            }
        }
        return productsWithoutStock;
    }

    public Pair<BigDecimal, BigDecimal> computeFinalValues(List<CartProduct> cartProducts, List<ProductDTO> warehouseStock) {

        BigDecimal totalWeight = new BigDecimal(0);
        BigDecimal totalPrice = new BigDecimal(0);

        Map<Long, ProductDTO> productMap = warehouseStock.stream()
                .collect(Collectors.toMap(ProductDTO::getId, Function.identity()));


        for (CartProduct cartProduct : cartProducts) {
            int quantity = cartProduct.getQuantity();
            ProductDTO warehouseProduct = productMap.get(cartProduct.getProduct().getId());
            totalWeight = totalWeight.add(warehouseProduct.getWeight().multiply(BigDecimal.valueOf(quantity)));
            totalPrice = totalPrice.add(warehouseProduct.getPrice().multiply(BigDecimal.valueOf(quantity)));

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
