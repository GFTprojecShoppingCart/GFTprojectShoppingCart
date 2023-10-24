package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.model.CartProduct;
import com.gftproject.shoppingcart.model.ProductDTO;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CartComputationsService {

    public Pair<BigDecimal, BigDecimal> computeFinalValues(List<CartProduct> cartProducts, List<ProductDTO> warehouseStock) {

        BigDecimal totalWeight = new BigDecimal(0);
        BigDecimal totalPrice = new BigDecimal(0);

        Map<Long, ProductDTO> productMap = warehouseStock.stream()
                .collect(Collectors.toMap(ProductDTO::getId, Function.identity()));

        for (CartProduct cartProduct : cartProducts) {
            int quantity = cartProduct.getQuantity();
            ProductDTO warehouseProduct = productMap.get(cartProduct.getProduct());
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

    public BigDecimal applyTaxes(BigDecimal originalPrice, BigDecimal weight, double cardPercentage, double countryPercentage) {

        BigDecimal priceWithTaxes = new BigDecimal(0);
        priceWithTaxes = priceWithTaxes.add(originalPrice);

        double weightPercentage = computeByWeight(weight.doubleValue());

        BigDecimal finalWeightPrice = priceWithTaxes.multiply(BigDecimal.valueOf(weightPercentage));
        BigDecimal finalCardPrice = priceWithTaxes.multiply(BigDecimal.valueOf(cardPercentage));
        BigDecimal finalCountryPrice = priceWithTaxes.multiply(BigDecimal.valueOf(countryPercentage));

        priceWithTaxes = priceWithTaxes.add(finalCardPrice);
        priceWithTaxes = priceWithTaxes.add(finalWeightPrice);
        priceWithTaxes = priceWithTaxes.add(finalCountryPrice);

        return priceWithTaxes;
    }

}
