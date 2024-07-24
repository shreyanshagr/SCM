package com.scm.service;

import com.scm.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveUser(User user);

    Optional<User> updateUser(User user);

    Optional<User> getUserById(String userId);

    void deleteUser(String userId);

    boolean isUserExist(String userId);

    boolean isUserExistByEmail(String email);

    Optional<List<User>> getAllUsers();

    User getUserByEmail(String email);

}
