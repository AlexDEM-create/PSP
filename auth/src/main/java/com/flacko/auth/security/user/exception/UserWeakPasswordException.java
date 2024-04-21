package com.flacko.auth.security.user.exception;

public class UserWeakPasswordException extends Exception {

    public UserWeakPasswordException(String login) {
        super(String.format("The provided password for user %s is too weak. " +
                "Please choose a password with at least 10 characters.", login));
    }

}
