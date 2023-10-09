package com.gftproject.shoppingcart;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class ShoppingcartApplicationTests {

	@Test
	void contextLoads() {
		
	}

	@Test
	@DirtiesContext //to avoid giving problems to the test "findCartById"
	public void deleteCartById() {
		shoppingCartRepository.deleteById(001);
		assertNull(shoppingCartRepository.findAllById(001));

	}

}
