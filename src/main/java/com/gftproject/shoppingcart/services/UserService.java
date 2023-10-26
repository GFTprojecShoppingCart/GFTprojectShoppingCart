package com.gftproject.shoppingcart.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gftproject.shoppingcart.exceptions.UserNotFoundException;
import com.gftproject.shoppingcart.model.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;

@Service
public class UserService{

    @Value("${spring.app-properties.userEndpoint}")
    private String endpoint;

    private static final Logger logger = Logger.getLogger(UserService.class);

    public User getUserById(Long userId) throws UserNotFoundException {
        String fullEndpoint = endpoint + "/users/" + userId;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(fullEndpoint);
            HttpResponse response = httpClient.execute(request);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectReader objectReader = objectMapper.readerFor(new TypeReference<Map<String, Object>>() {});
                Map<String, Object> jsonMap = objectReader.readValue(responseBody);

                User user = new User();
                user.setUserID(Long.parseLong(jsonMap.get("id").toString()));

                Map<String, Object> address = (Map<String, Object>) jsonMap.get("address");
                Map<String, Object> country = (Map<String, Object>) address.get("country");
                user.setCountry(country.get("name").toString());

                Map<String, Object> paymentMethod = (Map<String, Object>) jsonMap.get("paymentMethod");
                user.setPaymentMethod(paymentMethod.get("name").toString());

                return user;

            } else if (statusCode == 404) {
                throw new UserNotFoundException(String.valueOf(userId));
            } else {
                logger.error("Error: HTTP Status Code " + statusCode + " for URL: " + fullEndpoint);
                return null;
            }
        } catch (IOException e) {
            logger.error("An error occurred while making an HTTP request to " + fullEndpoint, e);
            return null;
        }
    }

}
