
package com.flacko.appeal.exception;

public class AppealNotFoundException extends Exception {

    public AppealNotFoundException(String id) {
        super(String.format("Appeal %s not found", id));
    }
}