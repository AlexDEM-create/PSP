package com.flacko.common.exception;

import java.util.Optional;

public class TraderTeamMissingRequiredAttributeException extends Exception {

    public TraderTeamMissingRequiredAttributeException(String attributeName, Optional<String> id) {
        super(String.format("Missing required %s attribute for trader team %s", attributeName,
                id.orElse("unknown")));
    }

}
