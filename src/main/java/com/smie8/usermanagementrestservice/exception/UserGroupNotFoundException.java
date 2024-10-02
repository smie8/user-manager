package com.smie8.usermanagementrestservice.exception;

public class UserGroupNotFoundException extends RuntimeException {
    public UserGroupNotFoundException(Long id) {
        super("Could not find user group " + id);
    }
}
