package com.gftproject.shoppingcart;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;
import com.gftproject.shoppingcart.repositories.ShoppingCartRepository;
import com.gftproject.shoppingcart.services.ShoppingCartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShoppingcartApplication {


	public static void main(String[] args) {
		SpringApplication.run(ShoppingcartApplication.class, args);
		
	}

}
