package com.flacko.appeal.service.exception;

import java.util.Optional;

public class AppealMissingRequiredAttributeException extends Exception {

    public AppealMissingRequiredAttributeException(String attributeName, Optional<String> id) {
        super(String.format("Missing required %s attribute for appeal %s", attributeName, id));
    }

}
