package com.smie8.usermanager.service;

import com.smie8.usermanager.model.UserGroup;

import java.util.List;
import java.util.Optional;

public interface UserGroupService {

    List<UserGroup> getAllUserGroups();
    Optional<UserGroup> getUserGroupById(Long id);

    UserGroup createUserGroup(UserGroup userGroup);

    void deleteUserGroup(Long id);

    void addUserToGroup(Long userId, Long groupId);
    void removeUserFromGroup(Long userId, Long groupId);
    void removeAllUsersFromGroup(Long groupId);
}
