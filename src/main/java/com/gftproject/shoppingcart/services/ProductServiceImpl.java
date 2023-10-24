package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.model.CartProduct;
import com.gftproject.shoppingcart.model.ProductDTO;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

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
    public ProductDTO getProductById(Long productId) throws ProductNotFoundException {

        String fullUrl = apiUrl + "/getProductById";

        try {

            String jsonBody = "{\"productId\": " + productId + "}";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<ProductDTO> responseEntity = restTemplate.exchange(fullUrl, HttpMethod.POST, requestEntity, ProductDTO.class);
            
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

/*    @Override
    public List<ProductRequest> getProductsByIds(List<Long> productIds) throws ProductNotFoundException {
        try {
            String url = apiUrl + "/getProductsByIds";
            // Construir el cuerpo de la solicitud JSON con la lista de productIds
            String jsonBody = "{\"productIds\": " + productIds + "}";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<List<ProductRequest>> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {
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
    }*/

    public List<ProductDTO> submitPurchase(List<CartProduct> productList) throws ProductNotFoundException, NotEnoughStockException {
        try {
            String url = apiUrl + "/getProductsToSubmit";

            JSONArray productArray = new JSONArray();

            for (CartProduct product : productList) {
                JSONObject productObject = new JSONObject();

                productObject.put("quantity", product.getQuantity());
                productObject.put("productId", product.getProduct());

                productArray.put(productObject);
                }
        
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
    
            HttpEntity<String> requestEntity = new HttpEntity<>(productArray.toString(), headers);
            ResponseEntity<List<ProductDTO>> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});
            
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
