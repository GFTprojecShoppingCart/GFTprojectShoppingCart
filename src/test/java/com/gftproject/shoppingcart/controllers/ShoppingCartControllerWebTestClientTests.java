package com.gftproject.shoppingcart.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gftproject.shoppingcart.model.Cart;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

            client.get().uri("/carts/1").exchange()
                    .expectStatus().isNotFound();

    }

//    @Test
//    @Order(2)
//    @DisplayName("GIVEN userId WHEN findAllByStatus is executed THEN list carts by status")
//
//    void findAll() {
//        client.get().uri("/carts/").exchange()
//                .expectStatus().isOk()
//                .expectHeader().contentType(MediaType.APPLICATION_JSON)
//                .expectBodyList(Cart.class)
//                .consumeWith(response-> {
//                    List<Cart> carts = response.getResponseBody();
//                });
//                assertEquals(2, carts.size());
//                assertEquals();






//        client.get().uri("/carts/").exchange()
//                .expectStatus().isOk()
//                .expectHeader().contentType(APPLICATION_JSON)
//                .expectBody()
//                .jsonPath("$[0].cart").isEqualTo("SUMMITED")
//                .jsonPath("$[0].id").isEqualTo("1L")


//    }
}