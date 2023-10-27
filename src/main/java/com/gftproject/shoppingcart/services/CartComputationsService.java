package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.model.CartProduct;
import com.gftproject.shoppingcart.model.ProductDTO;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CartComputationsService {

    private final Map<Double, Double> weightCostMap = new HashMap<>();
    double defaultValue;

    public CartComputationsService(){
        weightCostMap.put(5.0, 5.0);
        weightCostMap.put(10.0, 10.0);
        weightCostMap.put(20.0, 20.0);
        defaultValue = 50.0;

    }

    public Pair<BigDecimal, BigDecimal> computeFinalWeightAndPrice(List<CartProduct> cartProducts, List<ProductDTO> warehouseStock) {

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

    public double computeTaxByWeight(double cartWeight) {

        for (double threshold : weightCostMap.keySet()) {
            if (cartWeight <= threshold) {
                return weightCostMap.get(threshold);
            }
        }
        return defaultValue;
    }

    public BigDecimal applyTaxes(BigDecimal originalPrice, double weightPercentage, double cardPercentage, double countryPercentage) {

        BigDecimal priceWithTaxes = new BigDecimal(0);
        priceWithTaxes = priceWithTaxes.add(originalPrice);

        BigDecimal finalWeightPrice = priceWithTaxes.multiply(BigDecimal.valueOf(weightPercentage / 100.0));
        BigDecimal finalCardPrice = priceWithTaxes.multiply(BigDecimal.valueOf(cardPercentage / 100.0));
        BigDecimal finalCountryPrice = priceWithTaxes.multiply(BigDecimal.valueOf(countryPercentage / 100.0));

        priceWithTaxes = priceWithTaxes.add(finalCardPrice);
        priceWithTaxes = priceWithTaxes.add(finalWeightPrice);
        priceWithTaxes = priceWithTaxes.add(finalCountryPrice);

        return priceWithTaxes;
    }

}
