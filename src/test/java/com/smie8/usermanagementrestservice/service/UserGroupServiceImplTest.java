package com.smie8.usermanagementrestservice.service;

import com.smie8.usermanagementrestservice.model.User;
import com.smie8.usermanagementrestservice.model.UserGroup;
import com.smie8.usermanagementrestservice.repository.UserGroupRepository;
import com.smie8.usermanagementrestservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserGroupServiceImplTest {

    public static final long USER_ID = 2L;
    public static final long USER_GROUP_ID = 1L;
    @Mock
    private UserGroupRepository userGroupRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserGroupServiceImpl userGroupService;

    private User user;
    private UserGroup userGroup;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(USER_ID);

        userGroup = new UserGroup();
        userGroup.setId(USER_GROUP_ID);
        userGroup.setName("Group");
    }

    @Test
    public void testGetAllUserGroups() {
        List<UserGroup> userGroups = List.of(new UserGroup(), new UserGroup());
        when(userGroupRepository.findAll()).thenReturn(userGroups);

        List<UserGroup> result = userGroupService.getAllUserGroups();

        verify(userGroupRepository).findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAllUserGroupsEmpty() {
        when(userGroupRepository.findAll()).thenReturn(List.of());

        List<UserGroup> result = userGroupService.getAllUserGroups();

        verify(userGroupRepository).findAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetUserGroupById() {
        when(userGroupRepository.findById(USER_GROUP_ID)).thenReturn(Optional.of(userGroup));

        Optional<UserGroup> result = userGroupService.getUserGroupById(USER_GROUP_ID);

        verify(userGroupRepository).findById(USER_GROUP_ID);
        assertTrue(result.isPresent());
        assertEquals(USER_GROUP_ID, result.get().getId());
    }

    @Test
    public void testGetUserGroupByIdNull() {
        assertThrows(IllegalArgumentException.class, () -> userGroupService.getUserGroupById(null));
    }

    @Test
    public void testCreateUserGroup() {
        when(userGroupRepository.save(userGroup)).thenReturn(userGroup);

        UserGroup result = userGroupService.createUserGroup(userGroup);

        verify(userGroupRepository).save(userGroup);
        assertNotNull(result);
        assertEquals("Group", result.getName());
    }

    @Test
    public void testCreateUserGroupEmptyName() {
        UserGroup userGroup = new UserGroup();
        userGroup.setName("");

        assertThrows(IllegalArgumentException.class, () -> userGroupService.createUserGroup(userGroup));
    }

    @Test
    public void testDeleteUserGroup() {
        when(userGroupRepository.existsById(USER_GROUP_ID)).thenReturn(true);

        userGroupService.deleteUserGroup(USER_GROUP_ID);

        verify(userGroupRepository).deleteById(USER_GROUP_ID);
    }

    @Test
    public void testDeleteUserGroupNull() {
        assertThrows(IllegalArgumentException.class, () -> userGroupService.deleteUserGroup(null));
    }

    @Test
    public void testAddUserToGroup() {
        when(userRepository.findById(USER_GROUP_ID)).thenReturn(Optional.of(user));
        when(userGroupRepository.findById(USER_ID)).thenReturn(Optional.of(userGroup));

        userGroupService.addUserToGroup(USER_GROUP_ID, USER_ID);

        verify(userGroupRepository).save(userGroup);
    }

    @Test
    public void testAddUserToGroupNullUserId() {
        assertThrows(IllegalArgumentException.class, () -> userGroupService.addUserToGroup(null, USER_GROUP_ID));
    }

    @Test
    public void testAddUserToGroupNullGroupId() {
        assertThrows(IllegalArgumentException.class, () -> userGroupService.addUserToGroup(USER_GROUP_ID, null));
    }

    @Test
    public void testRemoveUserFromGroup() {
        // First add user to group
        userGroup.getUsers().add(user);
        assert(userGroup.getUsers().contains(user));

        when(userRepository.findById(USER_GROUP_ID)).thenReturn(Optional.of(user));
        when(userGroupRepository.findById(USER_ID)).thenReturn(Optional.of(userGroup));

        userGroupService.removeUserFromGroup(USER_GROUP_ID, USER_ID);

        verify(userRepository).findById(USER_GROUP_ID);
        verify(userGroupRepository).findById(USER_ID);
        verify(userGroupRepository).save(userGroup);
        assertFalse(userGroup.getUsers().contains(user));
        assertTrue(userGroup.getUsers().isEmpty());
    }
}