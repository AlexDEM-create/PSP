package com.flacko.balance.exception;

import com.flacko.auth.exception.NotFoundException;

public class BalanceNotFoundException extends NotFoundException {

    public BalanceNotFoundException(String id) {
        super(String.format("Balance %s not found", id));
    }

}
