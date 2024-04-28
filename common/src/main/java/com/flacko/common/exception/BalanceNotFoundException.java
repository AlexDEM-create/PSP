package com.flacko.common.exception;

public class BalanceNotFoundException extends NotFoundException {

    public BalanceNotFoundException(String id) {
        super(String.format("Balance %s not found", id));
    }

}
