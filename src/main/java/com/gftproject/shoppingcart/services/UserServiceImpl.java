package com.gftproject.shoppingcart.services;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;

import java.io.IOException;

@Service
public class UserServiceImpl implements UserService {

    @Value("${spring.app-properties.userEndpoint}")
    private String endpoint;

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Override
    public boolean validate(Long userId) {
        String fullEndpoint = endpoint + "/validateUser/" + userId;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(fullEndpoint);
            HttpResponse response = httpClient.execute(request);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                return true;
            } else if (statusCode == 404) {
                return false;
            } else {
                logger.error("Error: HTTP Status Code " + statusCode + " for URL: " + fullEndpoint);
                return false;
            }
        } catch (IOException e) {
            logger.error("An error occurred while making an HTTP request to " + fullEndpoint, e);
            return false;
        }
    }


}
