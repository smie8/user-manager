package com.smie8.usermanager.service;

import com.smie8.usermanager.model.User;
import com.smie8.usermanager.model.UserGroup;
import com.smie8.usermanager.repository.UserGroupRepository;
import com.smie8.usermanager.repository.UserRepository;
import com.smie8.usermanager.exception.UserGroupNotFoundException;
import com.smie8.usermanager.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserGroupServiceImpl implements UserGroupService {

    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;

    public UserGroupServiceImpl(UserGroupRepository userGroupRepository, UserRepository userRepository) {
        this.userGroupRepository = userGroupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserGroup> getAllUserGroups() {
        return userGroupRepository.findAll();
    }

    @Override
    public Optional<UserGroup> getUserGroupById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return userGroupRepository.findById(id);
    }

    @Override
    public UserGroup createUserGroup(UserGroup userGroup) {
        if (userGroup.getName() == null || userGroup.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        return userGroupRepository.save(userGroup);
    }

    @Override
    public void deleteUserGroup(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (!userGroupRepository.existsById(id)) {
            throw new UserGroupNotFoundException(id);
        }
        userGroupRepository.deleteById(id);
    }

    @Override
    public void addUserToGroup(Long userId, Long groupId) {
        validateUserAndGroupId(userId, groupId);

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        UserGroup userGroup = userGroupRepository.findById(groupId).orElseThrow(() -> new UserGroupNotFoundException(groupId));

        if (!userGroup.getUsers().contains(user)) {
            userGroup.getUsers().add(user);
            userGroup.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
            userGroupRepository.save(userGroup);
        } else {
            throw new IllegalArgumentException("User is already in the group");
        }
    }

    @Override
    public void removeUserFromGroup(Long userId, Long groupId) {
        validateUserAndGroupId(userId, groupId);

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        UserGroup userGroup = userGroupRepository.findById(groupId).orElseThrow(() -> new UserGroupNotFoundException(groupId));

        userGroup.getUsers().remove(user);
        userGroup.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        userGroupRepository.save(userGroup);
    }

    private static void validateUserAndGroupId(Long userId, Long groupId) {
        if (userId == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }

        if (groupId == null) {
            throw new IllegalArgumentException("Group id cannot be null");
        }
    }

    @Override
    public void removeAllUsersFromGroup(Long groupId) {
        if (groupId == null) {
            throw new IllegalArgumentException("Group id cannot be null");
        }
        UserGroup userGroup = userGroupRepository.findById(groupId).orElseThrow(() -> new UserGroupNotFoundException(groupId));
        userGroup.getUsers().clear();
        userGroupRepository.save(userGroup);
    }
}
