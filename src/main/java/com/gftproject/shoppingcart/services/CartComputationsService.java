package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Product;
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

    public void computeFinalValues(Cart cart) throws NotEnoughStockException {

        BigDecimal totalWeight = new BigDecimal(0);
        BigDecimal totalPrice = new BigDecimal(0);

        for (Map.Entry<Long, Integer> entry : cart.getProducts().entrySet()) {

            Long product = entry.getKey();
            int quantity = entry.getValue();

/*            if (quantity > entry.getKey().getStorageQuantity()) {
                throw new NotEnoughStockException();
            }*/

//            BigDecimal productWeight = product.getWeight();
//            BigDecimal productPrice = product.getPrice();
            BigDecimal productPrice = new BigDecimal("-1");
            BigDecimal productWeight = new BigDecimal("-1");

            totalWeight = totalWeight.add(productWeight.multiply(BigDecimal.valueOf(quantity)));
            totalPrice = totalPrice.add(productPrice.multiply(BigDecimal.valueOf(quantity)));

        }

        cart.setFinalWeight(totalWeight);
        cart.setFinalPrice(totalPrice);
    }

    public boolean cartValidity(Cart cart) {
        //TODO recorre todos los productos, y comprueba que hay suficiente stock
        return false;
    }

}
