package com.flacko.auth.security.user.exception;

public class UserLoginAlreadyInUseException extends Exception {

    public UserLoginAlreadyInUseException(String login) {
        super(String.format("The login '%s' is already in use. Please choose a different login.", login));
    }

}
