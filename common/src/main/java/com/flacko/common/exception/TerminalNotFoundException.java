package com.flacko.common.exception;

public class TerminalNotFoundException extends NotFoundException {

    public TerminalNotFoundException(String id) {
        super(String.format("Terminal %s not found", id));
    }

}
