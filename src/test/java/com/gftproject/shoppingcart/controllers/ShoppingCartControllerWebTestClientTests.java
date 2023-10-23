package com.gftproject.shoppingcart.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gftproject.shoppingcart.model.Cart;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
}