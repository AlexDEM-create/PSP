package com.flacko.incoming.payment.service.exception;

public class IncomingPaymentInvalidAmountException extends Exception {

    public IncomingPaymentInvalidAmountException(String id) {
        super(String.format("Amount cannot be less than 0 for payment %s", id));
    }

}
