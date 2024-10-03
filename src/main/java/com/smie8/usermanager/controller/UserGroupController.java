package com.smie8.usermanager.controller;

import com.smie8.usermanager.model.UserGroup;
import com.smie8.usermanager.service.UserGroupService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Get all user groups")
    @GetMapping(BASE_URL)
    public List<UserGroup> getAllUserGroups() {
        return userGroupService.getAllUserGroups();
    }

    @Operation(summary = "Get user group by id")
    @GetMapping(BASE_URL + "/{id}")
    public ResponseEntity<UserGroup> getUserGroupById(@PathVariable Long id) {
        Optional<UserGroup> userGroup = userGroupService.getUserGroupById(id);
        return userGroup.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create user group with a name using request body")
    @PostMapping(BASE_URL)
    public ResponseEntity<UserGroup> createUserGroup(@RequestBody UserGroup userGroup) {
        UserGroup createdUserGroup = userGroupService.createUserGroup(userGroup);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserGroup);
    }

    @Operation(summary = "Delete user group by id")
    @DeleteMapping(BASE_URL + "/{id}")
    public ResponseEntity<Void> deleteUserGroup(@PathVariable Long id) {
        userGroupService.deleteUserGroup(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Add user to group")
    @PostMapping(BASE_URL + "/{groupId}/users/{userId}")
    public ResponseEntity<Void> addUserToGroup(@PathVariable Long userId, @PathVariable Long groupId) {
        userGroupService.addUserToGroup(userId, groupId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove user from group")
    @DeleteMapping(BASE_URL + "/{groupId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromGroup(@PathVariable Long userId, @PathVariable Long groupId) {
        userGroupService.removeUserFromGroup(userId, groupId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove all users from group")
    @DeleteMapping(BASE_URL + "/{groupId}/users")
    public ResponseEntity<Void> removeAllUsersFromGroup(@PathVariable Long groupId) {
        userGroupService.removeAllUsersFromGroup(groupId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove user from all groups")
    @DeleteMapping(BASE_URL + "/users/{userId}")
    public ResponseEntity<Void> removeUserFromAllGroups(@PathVariable Long userId) {
        userGroupService.removeUserFromAllGroups(userId);
        return ResponseEntity.ok().build();
    }
}
