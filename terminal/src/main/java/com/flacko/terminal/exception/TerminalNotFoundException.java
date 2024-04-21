package com.flacko.terminal.exception;

import com.flacko.auth.exception.NotFoundException;

public class TerminalNotFoundException extends NotFoundException {

    public TerminalNotFoundException(String id) {
        super(String.format("Terminal %s not found", id));
    }

}
