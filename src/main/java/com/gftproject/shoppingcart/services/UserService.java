package com.gftproject.shoppingcart.services;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@Service
public class UserService {

    @Value("${spring.app-properties.userEndpoint}")
    private String endpoint;

    private static final Logger logger = Logger.getLogger(UserService.class);

    public User getUserById(Long userId) throws UserNotFoundException {
        String fullEndpoint = endpoint + "/validateUser/" + userId;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(fullEndpoint);
            HttpResponse response = httpClient.execute(request);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                return new ObjectMapper().readValue(responseBody, User.class);
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
