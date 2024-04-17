package com.flacko.auth.security.user.exception;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(String id) {
        super(String.format("User %s not found", id));
    }

}
