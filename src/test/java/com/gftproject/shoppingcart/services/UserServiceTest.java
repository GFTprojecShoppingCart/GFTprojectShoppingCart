package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.UserNotFoundException;
import com.gftproject.shoppingcart.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl();
    }

    @Test
    void getUserById() throws UserNotFoundException {
        //TODO mock the endpoint
        User user = userService.getUserById(1L);

        assertThat(user.getUserID()).isEqualTo(2L);
    }
}
