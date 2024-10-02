package com.smie8.usermanagementrestservice.controller;

import com.smie8.usermanagementrestservice.model.UserGroup;
import com.smie8.usermanagementrestservice.service.UserGroupService;
import com.smie8.usermanagementrestservice.exception.UserGroupNotFoundException;
import com.smie8.usermanagementrestservice.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.version.prefix}")
public class UserGroupController {
    private static final String BASE_URL = "/usergroup";

    private final UserGroupService userGroupService;

    public UserGroupController(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    @GetMapping(BASE_URL)
    public List<UserGroup> getAllUserGroups() {
        return userGroupService.getAllUserGroups();
    }

    @GetMapping(BASE_URL + "/{id}")
    public ResponseEntity<UserGroup> getUserGroupById(@PathVariable Long id) {
        Optional<UserGroup> userGroup = userGroupService.getUserGroupById(id);
        return userGroup.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(BASE_URL)
    public ResponseEntity<UserGroup> createUserGroup(@RequestBody UserGroup userGroup) {
        UserGroup createdUserGroup = userGroupService.createUserGroup(userGroup);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserGroup);
    }

    @DeleteMapping(BASE_URL + "/{id}")
    public ResponseEntity<Void> deleteUserGroup(@PathVariable Long id) {
        try {
            userGroupService.deleteUserGroup(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (UserGroupNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(BASE_URL + "/{groupId}/users/{userId}")
    public ResponseEntity<Void> addUserToGroup(@PathVariable Long userId, @PathVariable Long groupId) {
        try {
            userGroupService.addUserToGroup(userId, groupId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (UserNotFoundException | UserGroupNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(BASE_URL + "/{groupId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromGroup(@PathVariable Long userId, @PathVariable Long groupId) {
        try {
            userGroupService.removeUserFromGroup(userId, groupId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (UserNotFoundException | UserGroupNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(BASE_URL + "/{groupId}/users")
    public ResponseEntity<Void> removeAllUsersFromGroup(@PathVariable Long groupId) {
        try {
            userGroupService.removeAllUsersFromGroup(groupId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (UserGroupNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(BASE_URL + "/users/{userId}")
    public ResponseEntity<Void> removeUserFromAllGroups(@PathVariable Long userId) {
        try {
            userGroupService.removeUserFromAllGroups(userId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
