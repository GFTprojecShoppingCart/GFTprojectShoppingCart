package com.gftproject.shoppingcart.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Product;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


import org.springframework.test.web.reactive.server.WebTestClient;


import javax.print.attribute.standard.Media;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class ShoppingCartControllerWebTestClientTests {

    private ObjectMapper objectMapper;
    @Autowired
    private WebTestClient client;
    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
    }
    String userId = "1";
    @Test
    @DisplayName("GIVEN an user id WHEN a cart is created")
    void createCartTest() {

        // When
        client.post().uri("/carts/" + userId)
                .accept(APPLICATION_JSON) //acepto el tipo de contenido
                .exchange() //envia la solicitud HTTP al servidor.
                .expectStatus().isCreated();
    }


    @Test
    @DisplayName("GIVEN user id WHEN findAllByStatus is executed THEN list carts by status")

    void findAllByUserId() {
        final List<Cart> carts = new ArrayList<>(); // Declarar como final y efectivamente final

        client.get().uri("/carts/" + userId).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cart.class)
                .consumeWith(response -> {
                    carts.addAll(response.getResponseBody()); // Agregar elementos a la lista
                });

        assertNotNull(carts);
        assertEquals(2, carts.size()); // Verificar que haya 2 registros en la lista import.sql del usuario 1

        // Verificar el primer registro del user id 1
        assertEquals(1L, carts.get(0).getId());
        assertEquals("DRAFT", carts.get(0).getStatus().name());
        assertEquals(0, carts.get(0).getFinalPrice().intValue());
        assertEquals(0, carts.get(0).getFinalWeight().intValue());

        // Verificar el tercer registro del user id 1
        assertEquals(3L, carts.get(1).getId());
        assertEquals("SUBMITTED", carts.get(1).getStatus().name());
        assertEquals("4.50", carts.get(1).getFinalPrice().toPlainString());
        assertEquals(0, carts.get(1).getFinalWeight().intValue());

        client.get().uri("/carts/" + userId).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].status").isEqualTo("DRAFT")      // Verificar el estado del primer carrito
                .jsonPath("$[1].status").isEqualTo("SUBMITTED")  // Verificar el estado del segundo carrito
                .jsonPath("$[0].id").isEqualTo(1L)      // Verificar el ID del primer carrito
                .jsonPath("$[1].id").isEqualTo(3L);      // Verificar el ID del segundo carrito
    }


        @Test
        @DisplayName("GIVEN cartId WHEN deleteCart is executed THEN Delete a cart object")
        void deleteCart() throws Exception {

            client.get().uri("/carts/").exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON) //para validar la cabecera con contenido
                    // json
                    .expectBodyList(Cart.class)
                    .hasSize(4); //Esperamos 4 elementos en la lista de carritos del cliente

            client.delete().uri("/carts/1")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody().isEmpty();

            client.get().uri("/carts/").exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBodyList(Cart.class)
                    .hasSize(3);//Una vez eliminado el carrito, esperamos que haya 1 menos en la lista
    }
    @Test
    @Order(3)
    @DisplayName("GIVEN cartId WHEN updateCart is executed THEN update the cart")
    void updateProductsFromCart() throws Exception {
        List<Product> products = List.of(
                new Product(1L, new BigDecimal("19.99"), new BigDecimal("2.5"), 10),
                new Product(2L, new BigDecimal("9.99"), new BigDecimal("1.0"),5 )
        );

        client.put()
                .uri("/carts/updateStock/")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .bodyValue(products)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON);
                assertNotNull(products);

    }
    @Test
    @Order(4)
    @DisplayName("GIVEN cartId, productId, and quantity WHEN addProductToCart is executed THEN update the cart")
    void addProductToCart() throws Exception {
        Long cartId = 1L;
        Long productId = 2L;
        int quantity = 3;


        WebTestClient.ResponseSpec response = client.put()
                .uri("/carts/{cartId}/addProduct/{productId}?quantity={quantity}", cartId, productId, quantity)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        response.expectBody()
                .jsonPath("$.id").isNumber()
                .jsonPath("$.userId").isNumber()
                .jsonPath("$.status").isEqualTo("DRAFT")
                .jsonPath("$.finalPrice").isEqualTo(0.00)
                .jsonPath("$.finalWeight").isEqualTo(0.00);
    }

}