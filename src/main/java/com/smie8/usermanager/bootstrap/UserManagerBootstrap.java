package com.smie8.usermanager.bootstrap;

import com.smie8.usermanager.model.User;
import com.smie8.usermanager.model.UserGroup;
import com.smie8.usermanager.service.UserGroupService;
import com.smie8.usermanager.service.UserService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserManagerBootstrap {

    private final UserService userService;
    private final UserGroupService userGroupService;

    public UserManagerBootstrap(UserService userService, UserGroupService userGroupService) {
        this.userService = userService;
        this.userGroupService = userGroupService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createData();
        System.out.println("Bootstrap data initialized.");
    }

    private void createData() {
        // Add some users
        User user1 = new User();
        user1.setName("Sami");
        userService.createUser(user1);

        User user2 = new User();
        user2.setName("Seppo");
        userService.createUser(user2);

        // Add group and add user to group
        UserGroup userGroup1 = new UserGroup();
        userGroup1.setName("Group 1");
        userGroup1.getUsers().add(user1);
        userGroupService.createUserGroup(userGroup1);
    }
}
