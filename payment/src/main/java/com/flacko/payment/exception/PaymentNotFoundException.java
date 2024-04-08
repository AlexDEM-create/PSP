package com.flacko.payment.exception;

public class PaymentNotFoundException extends Exception {

    public PaymentNotFoundException(String id) {
        super(String.format("Payment %s not found", id));
    }

}
