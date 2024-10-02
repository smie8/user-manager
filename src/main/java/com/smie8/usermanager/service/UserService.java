package com.smie8.usermanager.service;

import com.smie8.usermanager.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();
    Optional<User> getUserById(Long id);

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);

    void addUserToGroup(Long userId, Long groupId);
    void removeUserFromGroup(Long userId, Long groupId);
}
