package com.flacko.terminal.service.exception;

import java.util.Optional;

public class TerminalMissingRequiredAttributeException extends Exception {

    public TerminalMissingRequiredAttributeException(String attributeName, Optional<String> id) {
        super(String.format("Missing required %s attribute for terminal %s", attributeName, id.orElse("unknown")));
    }

}
