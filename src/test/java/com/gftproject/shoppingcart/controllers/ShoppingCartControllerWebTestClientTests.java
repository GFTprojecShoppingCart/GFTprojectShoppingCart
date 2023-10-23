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

import java.math.BigDecimal;

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
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

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
        String userId = "1"; //  Creamos una variable de un userID que existe en nuestra db
        //int nonExistentUserId = 999; //  Creamos una variable de un userID que NO existe en nuestra db


    @Test
    @Order(1)
    @DisplayName("GIVEN an user id WHEN a cart is created")
    void createCartTest() {

        client.get().uri("/carts/").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON) //para validar la cabecera con contenido
                // json
                .expectBodyList(Cart.class)
                .hasSize(3); //Esperamos 3 elementos en la lista de carritos del cliente

        // When
        client.post().uri("/carts/" + userId)
                .accept(APPLICATION_JSON) //se acepta el tipo de contenido
                .exchange() //envia la solicitud HTTP al servidor.
                .expectStatus().isCreated();

        client.get().uri("/carts/").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cart.class)
                .hasSize(4);//Una vez añadido el carrito, esperamos que haya 1 más en la lista

//        // Se simula la creación de un carrito para un usuario que no existe
//        client.post().uri("/carts/" + nonExistentUserId)
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isNotFound(); // Esperamos que la respuesta sea 404 (Not Found)
    }


    @Test
    @Order(2)
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
        assertEquals(3, carts.size()); // Verificar que haya 2 registros en la lista import.sql del usuario 1

        // Verificar el primer registro del user id 1
        assertThat(carts.get(0).getId(), is(1L));
        assertThat(carts.get(0).getStatus().name(), is("DRAFT"));
        assertThat(carts.get(0).getFinalPrice().intValue(), is(0));
        assertThat(carts.get(0).getFinalWeight().intValue(), is(0));

        // Verificar el tercer registro del user id 1
        assertThat(carts.get(1).getId(), is(3L));
        assertThat(carts.get(1).getStatus().name(), is("SUBMITTED"));
        assertThat(carts.get(1).getFinalPrice(), is(new BigDecimal("4.50")));
        assertThat(carts.get(1).getFinalWeight().intValue(), is(0));
    }


    @Test
    @Order(3)
    @DisplayName("GIVEN cartId WHEN deleteCart is executed THEN Delete a cart object")
    void deleteCart() throws Exception {

        client.get().uri("/carts/").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON) //para validar la cabecera con contenido
                // json
                .expectBodyList(Cart.class)
                .hasSize(4); //Esperamos 4 elementos en la lista de carritos del cliente

        client.delete().uri("/carts/" +  userId)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();

        client.get().uri("/carts/").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cart.class)
                .hasSize(3);//Una vez eliminado el carrito, esperamos que haya 1 menos en la lista

//                    client.get().uri("/carts/1").exchange()
//                    .expectStatus().isNotFound();

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