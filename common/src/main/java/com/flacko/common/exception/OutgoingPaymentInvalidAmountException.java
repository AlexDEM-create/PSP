package com.flacko.common.exception;

public class OutgoingPaymentInvalidAmountException extends Exception {

    public OutgoingPaymentInvalidAmountException(String id) {
        super(String.format("Amount cannot be less than 0 for outgoing payment %s", id));
    }

}
