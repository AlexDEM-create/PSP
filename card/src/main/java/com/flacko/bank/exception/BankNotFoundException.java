package com.flacko.bank.exception;

import com.flacko.auth.exception.NotFoundException;

public class BankNotFoundException extends NotFoundException {

    public BankNotFoundException(String id) {
        super(String.format("Bank %s not found", id));
    }

}
