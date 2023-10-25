package com.gftproject.shoppingcart.integration;

import com.gftproject.shoppingcart.model.Cart;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.gftproject.shoppingcart.model.ProductDTO;
import com.gftproject.shoppingcart.repositories.CartRepository;
import com.gftproject.shoppingcart.services.ProductService;
import com.gftproject.shoppingcart.services.ShoppingCartService;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import jakarta.annotation.PostConstruct;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;




@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ShoppingCartMicroServicesTest {

    public static WireMockServer wireMockServer = new WireMockServer(8887);

    @LocalServerPort
    private int port;
    private WebTestClient client;


    @BeforeAll
    static void setUp() {
        wireMockServer.start();
    }

    @AfterAll
    static void tearDown() {
        wireMockServer.stop();
    }

    @AfterEach
    void resetAll() {
        wireMockServer.resetAll();
    }

    @PostConstruct
    void init() {
        client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:%d".formatted(port))
                .build();
    }

    // Parámetros de solicitud
    long userId = 1L;
    long cartId = 1L;
    long productId = 2L;
    int quantity = 5;
    int quantity2 = 3;

    @Test
    @Order(1)
    @DisplayName("GIVEN an user id WHEN a cart is created")
    void testAddProductToCartWithQuantity() {
        // Configura el comportamiento de WireMock para simular la respuesta del servicio externo
        wireMockServer.stubFor(WireMock.post(WireMock.urlMatching("/products/getBasicInfo")).withRequestBody(WireMock.equalToJson("[2]"))
                            .willReturn(aResponse().withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody("{\"id\": 2, \"price\": 10.0, \"stock\": 100, \"weight\": 0.5}")));


        client.put()
            .uri("/{userId}/carts/{cartId}/addProduct/{productId}?quantity={quantity}",userId, cartId, productId,quantity)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNumber()
                .jsonPath("$.userId").isNumber()
                .jsonPath("$.status").isEqualTo("DRAFT")
                .jsonPath("$.finalPrice").isEqualTo(0.00)
                .jsonPath("$.finalWeight").isEqualTo(0.00);
    }


// Implementa los métodos auxiliares necesarios para realizar las operaciones (crear carrito, agregar productos, actualizar stock, verificar, eliminar carrito)

    @Test
    @Order(2)
    @DisplayName("GIVEN a user WHEN a cart is created GIVEN a cart id WHEN product is added THEN delete cart")
    void testAddProductToCartWithQuantityAndDeleteCart() {
        // Configura el comportamiento de WireMock para simular la respuesta del servicio externo
        wireMockServer.stubFor(WireMock.post(WireMock.urlMatching("/products/getBasicInfo")).withRequestBody(WireMock.equalToJson("[2]"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 2, \"price\": 10.0, \"stock\": 100, \"weight\": 0.5}")));


        client.get().uri("/carts/").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON) //para validar la cabecera con contenido
                // json
                .expectBodyList(Cart.class)
                .hasSize(3); //Esperamos 3 elementos en la lista de carritos del cliente

        // Crear un carrito
        client.post()
                .uri("/{userId}/carts/{cartId}/create", userId, cartId)
                .contentType(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        // Verificar que el carrito se ha creado
        client.get()
                .uri("/{userId}/carts/{cartId}/get", userId, cartId)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(cartId)
                .jsonPath("$.userId").isEqualTo(userId);

        // Intento to add 4 products with the same ID (not enough stock)
        client.put()
                .uri("/{userId}/carts/{cartId}/addProduct/{productId}?quantity={quantity}", userId, cartId, productId, quantity)
                .contentType(APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest(); // Assuming a 400 Bad Request status if not enough stock

        // Verify that nothing is added to the cart
        client.get()
                .uri("/{userId}/carts/{cartId}/get", userId, cartId)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.products").isEmpty();

        // Attempt to add 3 products with the same ID (enough stock)
        client.put()
                .uri("/{userId}/carts/{cartId}/addProduct/{productId}?quantity={quantity}", userId, cartId, productId, quantity2)
                .contentType(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        // Verify that the product with quantity 3 is added to the cart
        client.get()
                .uri("/{userId}/carts/{cartId}/get", userId, cartId)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.products[0].productId").isEqualTo(productId)
                .jsonPath("$.products[0].quantity").isEqualTo(quantity2);

        // Delete the cart
        client.delete()
                .uri("/{userId}/carts/{cartId}/delete", userId, cartId)
                .exchange()
                .expectStatus().isOk();

        // Verify that the cart is deleted
        client.get()
                .uri("/{userId}/carts/{cartId}/get", userId, cartId)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound(); // Assuming a 404 Not Found status if the cart is deleted
    }
}
