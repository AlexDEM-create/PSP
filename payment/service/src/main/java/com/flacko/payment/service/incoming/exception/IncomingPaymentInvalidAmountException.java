package com.flacko.payment.service.incoming.exception;

public class IncomingPaymentInvalidAmountException extends Exception {

    public IncomingPaymentInvalidAmountException(String id) {
        super(String.format("Amount cannot be less than 0 for incoming payment %s", id));
    }

}
