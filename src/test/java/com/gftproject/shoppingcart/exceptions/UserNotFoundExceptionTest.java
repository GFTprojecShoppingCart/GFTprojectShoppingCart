package com.gftproject.shoppingcart.exceptions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserNotFoundExceptionTest {

    @Test
    void testConstructorWithUserId() {
        Long userId = 12L;
        UserNotFoundException exception = new UserNotFoundException(userId);
        assertThat(exception)
            .isNotNull()
            .hasMessage("User not found in warehouse: " + userId);
    }

    @Test
    void testConstructorWithCustomMessage() {
        String customMessage = "User not found with ID:";
        long customNumber = 42L;
        customMessage += " " + customNumber;
        UserNotFoundException exception = new UserNotFoundException(customMessage);
        assertThat(exception)
            .isNotNull()
            .hasMessage(customMessage);
    }
}
