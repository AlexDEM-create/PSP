package com.flacko.common.exception;

public class IncomingPaymentNotFoundException extends NotFoundException {

    public IncomingPaymentNotFoundException(String id) {
        super(String.format("Incoming payment %s not found", id));
    }

}
