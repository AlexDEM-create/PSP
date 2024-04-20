package com.flacko.appeal.exception;

import java.util.Optional;

public class AppealMissingRequiredAttributeException extends Exception {

    public AppealMissingRequiredAttributeException(String attributeName, Optional<String> appealId) {
        super("Missing required " + attributeName + " attribute for appeal " + appealId);
    }
}
