package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.exceptions.UserNotFoundException;
import com.gftproject.shoppingcart.model.User;

import java.nio.file.attribute.UserPrincipalNotFoundException;

public interface UserService {
    User getUserById(Long userId) throws UserNotFoundException;
}
