package com.flacko.auth.user;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(String id) {
        super(String.format("User %s not found", id));
    }

}
