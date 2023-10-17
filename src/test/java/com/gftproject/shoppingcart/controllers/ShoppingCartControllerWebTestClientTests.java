package com.gftproject.shoppingcart.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ShoppingCartControllerWebTestClientTests {

    private WebTestClient webTestClient;

    @BeforeEach
    //se configura el cliente web en cada prueba. Sustituye al autowired
    void setUp() {
        webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    }

    @Test
    @DisplayName("GIVEN an user WHEN a cart is created")
    void createCartTest() {
        // Given
        //defino la variable userId para quien se creará la cart.
        String userId = "1";

        // When
        webTestClient.post().uri("/carts/" + userId)
                .accept(APPLICATION_JSON) //acepto el tipo de contenido
                .exchange() //envia la solicitud HTTP al servidor.
                .expectStatus().isCreated();
    }
}