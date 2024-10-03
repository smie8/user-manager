package com.smie8.usermanager.service;

import com.smie8.usermanager.model.User;
import com.smie8.usermanager.model.UserGroup;
import com.smie8.usermanager.repository.UserGroupRepository;
import com.smie8.usermanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    public static final long USER_ID = 1L;
    public static final long GROUP_ID = 2L;
    public static final long GROUP_ID_2 = 3L;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserGroupRepository userGroupRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserGroup userGroup;
    private UserGroup userGroup2;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(USER_ID);

        userGroup = new UserGroup();
        userGroup.setId(GROUP_ID);
        userGroup2 = new UserGroup();
        userGroup2.setId(GROUP_ID_2);
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        verify(userRepository).findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAllUsersEmpty() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> result = userService.getAllUsers();

        verify(userRepository).findAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setId(USER_ID);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(USER_ID);

        verify(userRepository).findById(USER_ID);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(USER_ID, result.get().getId());
    }

    @Test
    public void testGetUserByIdNotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(USER_ID);

        verify(userRepository).findById(USER_ID);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setName("Sami");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        verify(userRepository).save(user);
        assertNotNull(result);
        assertEquals("Sami", result.getName());
    }

    @Test
    public void testCreateUserEmptyName() {
        User user = new User();
        user.setName("");

        try {
            userService.createUser(user);
        } catch (IllegalArgumentException e) {
            assertEquals("Name cannot be empty", e.getMessage());
        }
    }

    @Test
    public void testDeleteUser() {
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        userService.deleteUser(USER_ID);

        verify(userRepository).deleteById(USER_ID);
    }

    @Test
    public void testDeleteUserNotFound() {
        when(userRepository.existsById(USER_ID)).thenReturn(false);

        try {
            userService.deleteUser(USER_ID);
        } catch (Exception e) {
            assertEquals("Could not find user 1", e.getMessage());
        }
    }

    @Test
    public void testAddUserToGroup() {
        User user = new User();
        user.setId(USER_ID);
        UserGroup userGroup = new UserGroup();
        userGroup.setId(GROUP_ID);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userGroupRepository.findById(GROUP_ID)).thenReturn(Optional.of(userGroup));

        userService.addUserToGroup(USER_ID, GROUP_ID);

        verify(userRepository).findById(USER_ID);
        verify(userGroupRepository).findById(GROUP_ID);
        assertTrue(userGroup.getUsers().contains(user));
    }

    @Test
    public void testRemoveUserFromGroup() {
        User user = new User();
        user.setId(USER_ID);
        UserGroup userGroup = new UserGroup();
        userGroup.setId(GROUP_ID);
        userGroup.getUsers().add(user);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userGroupRepository.findById(GROUP_ID)).thenReturn(Optional.of(userGroup));

        userService.removeUserFromGroup(USER_ID, GROUP_ID);

        verify(userRepository).findById(USER_ID);
        verify(userGroupRepository).findById(GROUP_ID);
        assertTrue(userGroup.getUsers().isEmpty());
    }

    @Test
    public void removeUserFromAllGroups() {
        userGroup.getUsers().add(user);
        userGroup2.getUsers().add(user);
        assert(userGroup.getUsers().contains(user));
        assert(userGroup2.getUsers().contains(user));
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userGroupRepository.findAll()).thenReturn(List.of(userGroup, userGroup2));

        userService.removeUserFromAllGroups(USER_ID);

        verify(userRepository).findById(USER_ID);
        verify(userGroupRepository).findAll();
        assertTrue(userGroup.getUsers().isEmpty());
        assertTrue(userGroup2.getUsers().isEmpty());
    }
}