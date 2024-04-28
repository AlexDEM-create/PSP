package com.flacko.user.service.exception;

import java.util.Optional;

public class UserMissingRequiredAttributeException extends Exception {

    public UserMissingRequiredAttributeException(String attributeName, Optional<String> id) {
        super(String.format("Missing required %s attribute for user %s", attributeName, id.orElse("unknown")));
    }

}
