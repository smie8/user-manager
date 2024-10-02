package com.smie8.usermanager.service;

import com.smie8.usermanager.exception.UserGroupNotFoundException;
import com.smie8.usermanager.model.User;
import com.smie8.usermanager.model.UserGroup;
import com.smie8.usermanager.repository.UserGroupRepository;
import com.smie8.usermanager.repository.UserRepository;
import com.smie8.usermanager.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;

    public UserServiceImpl(UserRepository userRepository, UserGroupRepository userGroupRepository) {
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return userRepository.findById(id);
    }

    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new UserNotFoundException(user.getId());
        }
        user.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
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
}
