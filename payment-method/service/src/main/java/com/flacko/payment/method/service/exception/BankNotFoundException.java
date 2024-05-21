package com.flacko.payment.method.service.exception;

public class BankNotFoundException extends Exception {

    public BankNotFoundException(String id) {
        super(String.format("Bank with id %s not found", id));
    }
}
