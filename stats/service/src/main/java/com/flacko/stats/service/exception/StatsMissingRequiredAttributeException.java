package com.flacko.stats.service.exception;

import java.util.Optional;

public class StatsMissingRequiredAttributeException extends Exception {

    public StatsMissingRequiredAttributeException(String attributeName, Optional<String> id) {
        super(String.format("Missing required %s attribute for stats %s", attributeName, id.orElse("unknown")));
    }

}
