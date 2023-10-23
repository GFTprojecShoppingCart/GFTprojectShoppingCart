package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.model.Product;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService{

    @Value("${spring.app-properties.productEndpoint}")
    private String apiUrl;

    private static final Logger logger = Logger.getLogger(ProductServiceImpl.class);


    private final RestTemplate restTemplate;

    public ProductServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
// WebClient

    @Override
    public Product getProductById(Long productId) throws ProductNotFoundException {

        String fullUrl = apiUrl + "/getProductById";

        try {

            String jsonBody = "{\"productId\": " + productId + "}";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<Product> responseEntity = restTemplate.exchange(fullUrl, HttpMethod.POST, requestEntity, Product.class);
            
            HttpStatusCode  httpStatusCode  = responseEntity.getStatusCode();

            if (httpStatusCode  == HttpStatus.NOT_FOUND) {
                // Manejar el caso de código de estado 404 (Not Found) -> Wrong Product Id
                throw new ProductNotFoundException(String.valueOf(productId));
            }

            return responseEntity.getBody();
            
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Manejar excepciones HTTP (4xx y 5xx) aquí
            // Puedes registrar, lanzar una excepción personalizada o tomar medidas específicas según tus necesidades.
            logger.error("Error: " + e + " for URL: " + fullUrl);

            //TODO devolver excepcion personalizada
            return null;
        }
    }

    @Override
    public List<Product> getProductsByIds(List<Long> productIds) throws ProductNotFoundException {
        try {
            String url = apiUrl + "/getProductsByIds";
            // Construir el cuerpo de la solicitud JSON con la lista de productIds
            String jsonBody = "{\"productIds\": " + productIds + "}";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<List<Product>> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {
            });
            
            HttpStatusCode  httpStatusCode  = responseEntity.getStatusCode();

            if (httpStatusCode  == HttpStatus.NOT_FOUND) {
                // Manejar el caso de código de estado 404 (Not Found) -> Wrong Product Id
                throw new ProductNotFoundException(responseEntity.getBody().toString());
            }
            
            return responseEntity.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Manejar excepciones HTTP (4xx y 5xx) aquí
            // Puedes registrar, lanzar una excepción personalizada o tomar medidas específicas según tus necesidades.
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Product> getProductsToSubmit(List<Product> productList) throws ProductNotFoundException, NotEnoughStockException {
        try {
            String url = apiUrl + "/getProductsToSubmit";
    
            // Crear una lista de objetos JSON para los productos y cantidades
            List<JSONObject> productObjects = new ArrayList<>();
            for (Product product : productList) {
                JSONObject productObject = new JSONObject();
                productObject.put("productId", product.getId());
                productObject.put("quantity", product.getStorageQuantity());
                productObjects.add(productObject);
}
    
            // Crear el objeto JSON principal con la lista de productos
            JSONObject requestBodyJson = new JSONObject();
            requestBodyJson.put("products", productObjects);
    
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
    
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson.toString(), headers);
            ResponseEntity<List<Product>> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<Product>>() {});
            
            HttpStatusCode  httpStatusCode  = responseEntity.getStatusCode();

            if (httpStatusCode  == HttpStatus.NOT_FOUND) {
                // Manejar el caso de código de estado 404 (Not Found) -> Wrong Product Id
                throw new ProductNotFoundException(responseEntity.getBody().toString());
            }

            if (httpStatusCode  == HttpStatus.INTERNAL_SERVER_ERROR) {
                // Manejar el caso de código de estado 500 (Internal Server Error) -> Not stock
                throw new NotEnoughStockException(responseEntity.getBody().toString());
            }
            
            return responseEntity.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Manejar excepciones HTTP (4xx y 5xx) aquí
            // Puedes registrar, lanzar una excepción personalizada o tomar medidas específicas según tus necesidades.
            e.printStackTrace();
            return null;
        }
    }


}
