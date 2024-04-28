package com.flacko.common.exception;

import java.util.Optional;

public class BalanceMissingRequiredAttributeException extends Exception {

    public BalanceMissingRequiredAttributeException(String attributeName, Optional<String> id) {
        super(String.format("Missing required %s attribute for balance %s", attributeName, id.orElse("unknown")));
    }

}
