package com.gftproject.shoppingcart.integration;

import com.gftproject.shoppingcart.model.CartProduct;
import com.gftproject.shoppingcart.repositories.CartProductsRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Status;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import jakarta.annotation.PostConstruct;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.assertj.core.api.Assertions.assertThat;




@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ShoppingCartMicroServicesTest {

    public static WireMockServer wireMockServer = new WireMockServer(8887);

    @LocalServerPort
    private int port;
    private WebTestClient client;

    @Autowired
    CartProductsRepository cartProductsRepository;


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

    @Test
    @Order(1)
    @DisplayName("GIVEN a user id, cart id, product id and quantity WHEN rquest to add product to cart THEN the product is added")
    void testAddProductToCartWithQuantity() {
        // Configura el comportamiento de WireMock para simular la respuesta del servicio externo
        wireMockServer.stubFor(WireMock.post(WireMock.urlMatching("/products/getBasicInfo")).withRequestBody(WireMock.equalToJson("[2]"))
                            .willReturn(aResponse().withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody("{\"id\": 2, \"price\": 10.0, \"stock\": 100, \"weight\": 0.5}")));

        // Parámetros de solicitud
        long userId = 1L;
        long cartId = 4L;
        long productId = 2L;
        int quantity = 5;

        // List all carts
        client.get().uri("/carts/").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON) //para validar la cabecera con contenido
                // json
                .expectBodyList(Cart.class)
                .hasSize(3); //Esperamos 3 elementos en la lista de carritos del cliente

        // Create one cart
        client.post().uri("/carts/{userId}", userId)
                .accept(APPLICATION_JSON) //se acepta el tipo de contenido
                .exchange() //envia la solicitud HTTP al servidor.
                .expectStatus().isCreated();


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
                .jsonPath("$.finalPrice").isEqualTo(10.00)
                .jsonPath("$.finalWeight").isEqualTo(0.50);

        
    }

    @Test
    @Order(2)
    @DisplayName("GIVEN a user id, cart id, product id, and quantity WHEN request to add product to cart THEN the NotEnoughStockException is thrown")
    void testAddProductToCartWithQuantityNoStock() {
        // Configura el comportamiento de WireMock para simular la respuesta del servicio externo
        wireMockServer.stubFor(WireMock.post(WireMock.urlMatching("/products/getBasicInfo"))
                .withRequestBody(WireMock.equalToJson("[2]"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 2, \"price\": 10.0, \"stock\": 4, \"weight\": 0.5}"))); // Stock insuficiente (4)
    
        // Parámetros de solicitud
        long userId = 1L;
        long cartId = 4L;
        long productId = 2L;
        int quantity = 5;

        client.put()
                .uri("/{userId}/carts/{cartId}/addProduct/{productId}?quantity={quantity}", userId, cartId, productId, quantity)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT) // Esperamos un conflicto (HTTP 409)
                    .expectBody()
                    .jsonPath("$.error").isEqualTo("NOT ENOUGH STOCK ERROR")
                    .jsonPath("$.message").isEqualTo("Not enough stock of products with Id: [" + productId + "]");
    
    }

    @Test
    @Order(3)
    @DisplayName("GIVEN the ID of an existing cart WHEN submitCart is executed")
    void submitCart() {

        wireMockServer.stubFor(WireMock.post(WireMock.urlMatching("/products/reduceStock"))
                .withRequestBody(WireMock.equalToJson("[{\"id\": 2, \"stock\":5 }]")) // Update the JSON request body here
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\": 2, \"price\": 10.0, \"stock\": 95, \"weight\": 0.5}]")));

        wireMockServer.stubFor(WireMock.get(WireMock.urlMatching("/users/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "    \"id\": 1,\n" +
                                "    \"name\": \"John\",\n" +
                                "    \"lastName\": \"Doe\",\n" +
                                "    \"address\": {\n" +
                                "        \"id\": 1,\n" +
                                "        \"street\": \"Sunset Blvd\",\n" +
                                "        \"city\": \"Barcelona\",\n" +
                                "        \"province\": \"Catalonia\",\n" +
                                "        \"postalCode\": 12345,\n" +
                                "        \"country\": {\n" +
                                "            \"id\": 1,\n" +
                                "            \"name\": \"Spain\"\n" +
                                "        }\n" +
                                "    },\n" +
                                "    \"paymentMethod\": {\n" +
                                "        \"id\": 1,\n" +
                                "        \"name\": \"Credit Card\"\n" +
                                "    },\n" +
                                "    \"fidelityPoints\": 100,\n" +
                                "    \"averagePurchase\": 75.5\n" +
                                "}"
                            )));

        long cartId = 4L;

        client.put()
                .uri("/carts/{cartId}",cartId)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                    .expectBody(Cart.class)
                    .consumeWith(response -> {
                        Cart cart = response.getResponseBody();
                        assertThat(cart.getStatus()).isEqualTo(Status.SUBMITTED);
                        assertThat(cart.getFinalPrice()).isNotZero();
                        assertThat(cart.getFinalWeight()).isNotZero();
                    });
    }

    @Test
    @Order(4)
    @DisplayName("GIVEN cart with products WHEN update stock THEN product not enough stock")
    void testCreateCartAddProductAndUpdateStock() {
        wireMockServer.stubFor(WireMock.post(WireMock.urlMatching("/products/getBasicInfo")).withRequestBody(WireMock.equalToJson("[2]"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 2, \"price\": 10.0, \"stock\": 100, \"weight\": 0.5}")));

        long userId = 1L;
        long cartId = 1L;
        long productId = 2L;
        int quantity = 5;

        Cart createdCart = client.post()
                .uri("/carts/{userId}", userId)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Cart.class)
                .returnResult()
                .getResponseBody();

        client.put()
                .uri("/{userId}/carts/{cartId}/addProduct/{productId}?quantity={quantity}", userId, cartId, productId, quantity)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNumber()
                .jsonPath("$.userId").isEqualTo(userId)
                .jsonPath("$.status").isEqualTo("DRAFT")
                .jsonPath("$.finalPrice").isEqualTo(10.00)
                .jsonPath("$.finalWeight").isEqualTo(0.50);

        List<CartProduct> cartProducts = cartProductsRepository.findAllByCartId(createdCart.getId());

        client.put()
                .uri("/carts/updateStock/")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .bodyValue(cartProducts)
                .exchange()
                .expectStatus().isOk();

        wireMockServer.verify(WireMock.postRequestedFor(WireMock.urlEqualTo("/products/getBasicInfo")));



        for (CartProduct cartProduct : cartProducts) {
            assertFalse(cartProduct.isValid());

            client.put()
                    .uri("/{userId}/carts/{cartId}/submitCart", userId, cartId)
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.status").isEqualTo("DRAFT");
        }
    }
    
    
}
