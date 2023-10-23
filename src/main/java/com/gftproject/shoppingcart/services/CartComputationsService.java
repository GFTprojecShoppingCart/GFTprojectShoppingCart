package com.gftproject.shoppingcart.services;

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

    public List<Long> getProductIdsWithoutStock(List<Product> productList) {
        List<Long> productsWithoutStock = new ArrayList<>();
        for (Product product : productList) {
            if (product.getQuantity() > product.getStorageQuantity()) {
                productsWithoutStock.add(product.getId());
            }
        }
        return productsWithoutStock;
    }

    public Pair<BigDecimal, BigDecimal> computeFinalValues(List<Product> cartProducts, List<ProductDTO> warehouseStock) {

        BigDecimal totalWeight = new BigDecimal(0);
        BigDecimal totalPrice = new BigDecimal(0);

        Map<Long, ProductDTO> productMap = warehouseStock.stream()
                .collect(Collectors.toMap(ProductDTO::getId, Function.identity()));


        for (Product product : cartProducts) {

            int quantity = product.getQuantity();
            ProductDTO warehouseProduct = productMap.get(product.getId());
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
