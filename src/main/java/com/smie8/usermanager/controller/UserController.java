package com.smie8.usermanager.controller;

import com.smie8.usermanager.model.User;
import com.smie8.usermanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.version.prefix}")
public class UserController {

    public static final String BASE_URL = "/user";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users")
    @GetMapping(BASE_URL)
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Get user by id")
    @GetMapping(BASE_URL + "/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create user with a name using request body")
    @PostMapping(BASE_URL)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(summary = "Add user to group")
    @PostMapping(BASE_URL + "/{userId}/groups/{groupId}")
    public ResponseEntity<Void> addUserToGroup(@PathVariable Long userId, @PathVariable Long groupId) {
        userService.addUserToGroup(userId, groupId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update user by using request body")
    @PutMapping(BASE_URL + "/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        if (!id.equals(user.getId())) {
            return ResponseEntity.badRequest().build();
        }

        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Delete user by id")
    @DeleteMapping(BASE_URL + "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove user from group")
    @DeleteMapping(BASE_URL + "/{userId}/groups/{groupId}")
    public ResponseEntity<Void> removeUserFromGroup(@PathVariable Long userId, @PathVariable Long groupId) {
        userService.removeUserFromGroup(userId, groupId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove user from all groups")
    @DeleteMapping(BASE_URL + "/{userId}/groups")
    public ResponseEntity<Void> removeUserFromAllGroups(@PathVariable Long userId) {
        userService.removeUserFromAllGroups(userId);
        return ResponseEntity.ok().build();
    }
}
