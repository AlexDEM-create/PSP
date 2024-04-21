package com.flacko.auth.security.user.exception;

import com.flacko.auth.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(String id) {
        super(String.format("User %s not found", id));
    }

}
