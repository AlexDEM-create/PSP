package com.flacko.common.exception;

public class BankNotFoundException extends NotFoundException {

    public BankNotFoundException(String id) {
        super(String.format("Bank %s not found", id));
    }

}
