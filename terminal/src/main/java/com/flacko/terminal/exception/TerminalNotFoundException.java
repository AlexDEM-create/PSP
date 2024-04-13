package com.flacko.terminal.exception;

public class TerminalNotFoundException extends Exception {

    public TerminalNotFoundException(String id) {
        super(String.format("Terminal %s not found", id));
    }

}
