package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Product;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartComputationsService {

    public List<Long> checkStock(Map<Long, Integer> cartProducts, List<Product> warehouseStock) {

        List<Long> productsWithoutStock = new ArrayList<>();

        for (Product product : warehouseStock) {
            int productInCart = cartProducts.get(product.getId());
            if (product.getStorageQuantity() < productInCart) {
                productsWithoutStock.add(product.getId());
            }
        }
        return productsWithoutStock;
    }

    public Pair<BigDecimal, BigDecimal> computeFinalValues(Map<Long, Integer> cartProducts, List<Product> warehouseStock) throws NotEnoughStockException {

        BigDecimal totalWeight = new BigDecimal(0);
        BigDecimal totalPrice = new BigDecimal(0);

        for (Map.Entry<Long, Integer> entry : cartProducts.entrySet()) {
            long productId = entry.getKey();
            int quantity = entry.getValue();

            // Find the product with the given ID in the warehouse stock
            Product product = findProductById(productId, warehouseStock);

            if (product == null) {
                // Product not found in the warehouse stock, you can throw an exception or handle it as needed.
                throw new NotEnoughStockException("Product not found in the warehouse: " + productId);
            }

            // Calculate the total weight and price for the product and add it to the totals
            totalWeight = totalWeight.add(product.getWeight().multiply(BigDecimal.valueOf(quantity)));
            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        }

        return new Pair<>(totalWeight, totalPrice);
    }

    // Helper method to find a product by ID in the warehouse stock
    private Product findProductById(long productId, List<Product> warehouseStock) {
        for (Product product : warehouseStock) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null; // Product not found
    }


    public boolean cartValidity(Cart cart) {
        //TODO recorre todos los productos, y comprueba que hay suficiente stock
        return false;
    }

}
