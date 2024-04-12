package com.flacko.trader.exception;

import java.util.Optional;

public class TraderMissingRequiredAttributeException extends Exception {
    public TraderMissingRequiredAttributeException(String attributeName, Optional<String> id) {
        super("Missing required " + attributeName + " attribute for trader " + id.orElse("unknown"));
    }
}

