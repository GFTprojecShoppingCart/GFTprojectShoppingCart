package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.model.Product;

import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService{

    String apiUrl = "localhost:8080";

    RestTemplate restTemplate = new RestTemplate();
// WebClient

    @Override
    public Product getProductById(Long productId) {
        try {
            String url = apiUrl + "/getProductById";
            String jsonBody = "{\"productId\": " + productId + "}";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<Product> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Product.class);
            return responseEntity.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Manejar excepciones HTTP (4xx y 5xx) aquí
            // Puedes registrar, lanzar una excepción personalizada o tomar medidas específicas según tus necesidades.
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Product> getProductsByIds(List<Long> productIds) {
        try {
            String url = apiUrl + "/getProductsByIds";
            // Construir el cuerpo de la solicitud JSON con la lista de productIds
            String jsonBody = "{\"productIds\": " + productIds + "}";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<List<Product>> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<Product>>() {});
            return responseEntity.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Manejar excepciones HTTP (4xx y 5xx) aquí
            // Puedes registrar, lanzar una excepción personalizada o tomar medidas específicas según tus necesidades.
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Product> getProductsToSubmit(Map<Long, Integer> product) throws ProductNotFoundException {
        try {
            String url = apiUrl + "/getProductsToSubmit";
    
            // Crear una lista de objetos JSON para los productos y cantidades
            List<JSONObject> productObjects = new ArrayList<>();
            for (Map.Entry<Long, Integer> entry : product.entrySet()) {
                Long productId = entry.getKey();
                Integer quantity = entry.getValue();
                JSONObject productObject = new JSONObject();
                productObject.put("productId", productId);
                productObject.put("quantity", quantity);
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
                throw new ProductNotFoundException();
            }

            if (httpStatusCode  == HttpStatus.INTERNAL_SERVER_ERROR) {
                // Manejar el caso de código de estado 500 (Internal Server Error) -> Not stock
                
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
