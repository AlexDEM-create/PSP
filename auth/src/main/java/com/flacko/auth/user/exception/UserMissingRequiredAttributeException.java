package com.flacko.auth.user.exception;

import java.util.Optional;

public class UserMissingRequiredAttributeException extends Exception {

    public UserMissingRequiredAttributeException(String attributeName, Optional<String> userId) {
        super("Missing required " + attributeName + " attribute for user " + userId);
    }

}
