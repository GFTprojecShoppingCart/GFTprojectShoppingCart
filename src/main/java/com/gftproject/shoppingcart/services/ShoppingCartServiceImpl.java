package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Override
    public List<Cart> findAllByStatus(Status status) {
        return null;
    }

    @Override
    public void deleteCart() {
        repository.deleteCartyId(001);
        assertull(repository.findById(001));
    }
}
