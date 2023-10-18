package com.gftproject.shoppingcart.services;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Value("${spring.app-properties.userEndpoint}")
    private String endpoint;

    @Override
    public void validate(Long userId) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

         endpoint += "/validateUser/" + userId;

        try {
            HttpGet request = new HttpGet(endpoint);
            HttpResponse response = httpClient.execute(request);

            // Check the HTTP response status code
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                // User exists
//                return true;
            } else if (statusCode == 404) {
                // User does not exist
//                return false;
            } else {
                // Handle other status codes or errors as needed
                System.out.println("Error: HTTP Status Code " + statusCode);
//                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
//            return false;
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
