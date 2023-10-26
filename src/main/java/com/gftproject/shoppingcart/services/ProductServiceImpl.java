package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.CartProduct;
import com.gftproject.shoppingcart.model.ProductDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final RestTemplate restTemplate;
    @Value("${spring.app-properties.productEndpoint}")
    private String apiUrl;

    public ProductServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
// WebClient

    @Override
    public ProductDTO getProductById(Long productId) throws ProductNotFoundException {

        try {
            String fullUrl = apiUrl + "/products/getBasicInfo";

            List<Long> lista = Collections.singletonList(productId);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<List<Long>> requestEntity = new HttpEntity<>(lista, headers);
            ResponseEntity<List<ProductDTO>> responseEntity = restTemplate.exchange(fullUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {
            });

            return responseEntity.getBody().get(0);
        }catch (HttpClientErrorException ex){
            if(ex.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                throw new ProductNotFoundException(ex.getMessage());
            }else{
                throw ex;
            }
        }



    }


    public List<ProductDTO> submitPurchase(List<CartProduct> productList) throws ProductNotFoundException, NotEnoughStockException {
        try{
        String url = apiUrl + "/products/reduceStock";

        JSONArray productArray = new JSONArray();

        for (CartProduct product : productList) {
            JSONObject productObject = new JSONObject();

            productObject.put("id", product.getProduct());
            productObject.put("stock", product.getQuantity());

            productArray.put(productObject);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(productArray.toString(), headers);
        ResponseEntity<List<ProductDTO>> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {
        });

        return responseEntity.getBody();
        }catch (HttpClientErrorException ex){
            if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                throw new ProductNotFoundException(ex.getMessage());
            }else if(ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)){
                throw new NotEnoughStockException(ex.getMessage());
            }else{
                throw ex;
            }
        }
    }


}

