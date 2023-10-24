package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.model.User;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    CloseableHttpClient httpClient;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl();
    }

    @Test
    void getUserById() {
        //TODO mock the endpoint
//        when(httpClient.execute(any())).thenReturn(CloseableHttpResponse()));


        User user = userService.getUserById(1L);

        assertThat(user.getUserID()).isEqualTo(2L);
    }
}
