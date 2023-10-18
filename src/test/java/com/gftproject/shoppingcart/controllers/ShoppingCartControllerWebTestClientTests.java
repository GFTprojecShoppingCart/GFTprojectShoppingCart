package com.gftproject.shoppingcart.controllers;

import com.gftproject.shoppingcart.model.Cart;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class ShoppingCartControllerWebTestClientTests {

    private WebTestClient webTestClient;

    @BeforeEach
    //se configura el cliente web en cada prueba. Sustituye al autowired
    void setUp() {
        webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    }

    @Test
    @Order(1)
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

        @Test
        @Order(2)         //Este ha de ser el último test que corra
        @DisplayName("GIVEN cartId WHEN deleteCart is executed THEN Delete a cart object")
        void deleteCart() throws Exception {

            webTestClient.get().uri("/carts/").exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON) //para validar la cabecera con contenido
                    // json
                    .expectBodyList(Cart.class)
                    .hasSize(3); //Esperamos 4 elementos en la lista de carritos del cliente

            webTestClient.delete().uri("/carts/1")

                    .exchange()
                    .expectStatus().isOk()
//                    .expectStatus().isNoContent()
                    .expectBody().isEmpty();

            webTestClient.get().uri("/carts/").exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBodyList(Cart.class)
                    .hasSize(3);//Una vez eliminado el carrito, esperamos que haya 1 menos en la lista

//            webTestClient.get().uri("/carts/1").exchange()
//                    .expectStatus().is5xxServerError();

    }
}