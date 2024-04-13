package com.flacko.terminal.exception;

import java.util.Optional;

public class TerminalMissingRequiredAttributeException extends Exception {

    public TerminalMissingRequiredAttributeException(String attributeName, Optional<String> terminalId) {
        super("Missing required " + attributeName + " attribute for terminal " + terminalId);
    }

}
