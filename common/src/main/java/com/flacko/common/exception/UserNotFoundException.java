package com.flacko.common.exception;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(String id) {
        super(String.format("User %s not found", id));
    }

}
