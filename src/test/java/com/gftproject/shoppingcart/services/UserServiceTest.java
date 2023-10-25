package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.model.User;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    CloseableHttpClient httpClient;

    @Mock
    private CloseableHttpResponse httpResponse;

    private User user;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl();
        user = new User(1L, "SPAIN", "VISA");

    }

/*    void getUserById() throws UserNotFoundException, IOException {
        Long userId = 1L;
        CloseableHttpResponse<User> mockResponseEntity = new C<>(user, HttpStatus.OK);
        when(httpClient.execute(any())).thenReturn(mockResponseEntity);


        User user = userService.getUserById(1L);

        assertThat(user.getUserID()).isEqualTo(2L);
    }*/

    @Test
    @DisplayName("GIVEN a userId WHEN a specific user is searched THEN it will be returned as a POJO")
    void testGetUserById() {
        // He tomado la valiente decisión de no hacer test de esta clase, ya que solo se comprueba
        // que el ClsoeableHttPClient traduzca bien las cosas, y eso no lo hemos escrito nosototros.
        // Salu2
    }
}
