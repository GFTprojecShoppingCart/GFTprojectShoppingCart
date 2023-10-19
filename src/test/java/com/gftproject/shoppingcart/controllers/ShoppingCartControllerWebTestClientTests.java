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

    @Test
    @Order(1)
    @DisplayName("GIVEN an user WHEN a cart is created")
    void createCartTest() {
        // Given
        //defino la variable userId para quien se creará la cart.
        String userId = "1";

        // When
        client.post().uri("/carts/" + userId)
                .accept(APPLICATION_JSON) //acepto el tipo de contenido
                .exchange() //envia la solicitud HTTP al servidor.
                .expectStatus().isCreated();
    }

    @Test
    @Order(2)
    @DisplayName("GIVEN userId WHEN findAllByStatus is executed THEN list carts by status")

    void findAllByUserId() {
        final List<Cart> carts = new ArrayList<>(); // Declarar como final y efectivamente final

        client.get().uri("/carts/{userId}").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cart.class)
                .consumeWith(response -> {
                    carts.addAll(response.getResponseBody()); // Agregar elementos a la lista
                });

//        assertNotNull(carts);
//        assertEquals(1, carts.size());
//            assertEquals(1L, carts.get(0).getId());
//            assertEquals("DRAFT", carts.get(0).getStatus());
//            assertEquals("0", carts.get(0).getFinalPrice().toPlainString());
//            assertEquals("0", carts.get(0).getFinalWeight().toPlainString());
//
//            assertEquals(1L, carts.get(0).getId());
//            assertEquals("SUBMITTED", carts.get(1).getStatus());
//            assertEquals("0", carts.get(1).getFinalPrice().toPlainString());
//            assertEquals("4.5", carts.get(1).getFinalWeight().toPlainString());

        assertNotNull(carts);
        assertEquals(2, carts.size()); // Verificar que haya 2 registros en la lista import.sql

// Verificar el primer registro
        assertEquals(1L, carts.get(0).getId());
        assertEquals("DRAFT", carts.get(0).getStatus());
        assertEquals(BigDecimal.ZERO, carts.get(0).getFinalPrice());
        assertEquals(BigDecimal.ZERO, carts.get(0).getFinalWeight());

// Verificar el segundo registro
        assertEquals(2L, carts.get(1).getId());
        assertEquals("DRAFT", carts.get(1).getStatus());
        assertEquals(BigDecimal.ZERO, carts.get(1).getFinalPrice());
        assertEquals(BigDecimal.ZERO, carts.get(1).getFinalWeight());

// Verificar el tercer registro
        assertEquals(3L, carts.get(2).getId());
        assertEquals("SUBMITTED", carts.get(2).getStatus());
        assertEquals(new BigDecimal("4.5"), carts.get(2).getFinalPrice());
        assertEquals(BigDecimal.ZERO, carts.get(2).getFinalWeight());


        client.get().uri("/carts/{userId}").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[1].cart").isEqualTo("SUMMITED")
                .jsonPath("$[1].cart").isEqualTo("DRAFT")
                .jsonPath("$[0].id").isEqualTo("1");


    }

        @Test
        @Order(3)         //Este ha de ser el último test que corra
        @DisplayName("GIVEN cartId WHEN deleteCart is executed THEN Delete a cart object")
        void deleteCart() throws Exception {

            client.get().uri("/carts/").exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON) //para validar la cabecera con contenido
                    // json
                    .expectBodyList(Cart.class)
                    .hasSize(3); //Esperamos 4 elementos en la lista de carritos del cliente

            client.delete().uri("/carts/1")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody().isEmpty();

            client.get().uri("/carts/").exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBodyList(Cart.class)
                    .hasSize(2);//Una vez eliminado el carrito, esperamos que haya 1 menos en la lista

//            client.get().uri("/carts/1").exchange()
//                    .expectStatus().isNotFound();

    }


}