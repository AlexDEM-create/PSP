package com.flacko.appeal.exception;

import com.flacko.auth.exception.NotFoundException;

public class AppealNotFoundException extends NotFoundException {

    public AppealNotFoundException(String id) {
        super(String.format("Appeal %s not found", id));
    }

}
