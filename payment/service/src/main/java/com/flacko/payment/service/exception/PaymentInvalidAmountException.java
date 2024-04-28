package com.flacko.payment.service.exception;

public class PaymentInvalidAmountException extends Exception {

    public PaymentInvalidAmountException(String id) {
        super(String.format("Amount cannot be less than 0 for payment %s", id));
    }

}
