package com.smie8.usermanager.exception;

public class UserGroupNotFoundException extends RuntimeException {

    public UserGroupNotFoundException(Long id) {
        super("Could not find user group " + id);
    }
}
