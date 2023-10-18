package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.model.Product;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    String apiUrl = "localhost:8080/getProducts/";
// WebClient

    @Override
    public Product getProductById(Long productId) {
        RestTemplate restTemplate = new RestTemplate();

        // Construct the API URL with query parameters for product IDs
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("productIds", productId);

        ResponseEntity<Product> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            // Handle the response status code and error scenarios here
            throw new RuntimeException("Failed to retrieve products by IDs: " + response.getStatusCode());
        }
    }

    @Override
    public List<Product> getProductsByIds(List<Long> productIds) {
        RestTemplate restTemplate = new RestTemplate();

        // Construct the API URL with query parameters for product IDs
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("productIds", productIds.toArray());

        ResponseEntity<List<Product>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Product>>() {}
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            // Handle the response status code and error scenarios here
            throw new RuntimeException("Failed to retrieve products by IDs: " + response.getStatusCode());
        }
    }


}
