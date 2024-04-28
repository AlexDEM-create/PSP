package com.flacko.common.exception;

public class AppealNotFoundException extends NotFoundException {

    public AppealNotFoundException(String id) {
        super(String.format("Appeal %s not found", id));
    }

}
