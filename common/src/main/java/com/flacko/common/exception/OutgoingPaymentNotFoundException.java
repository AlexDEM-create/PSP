package com.flacko.common.exception;

public class OutgoingPaymentNotFoundException extends NotFoundException {

    public OutgoingPaymentNotFoundException(String id) {
        super(String.format("Outgoing payment %s not found", id));
    }

}
