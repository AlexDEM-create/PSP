package com.flacko.common.exception;

public class PaymentNotFoundException extends NotFoundException {

    public PaymentNotFoundException(String id) {
        super(String.format("Payment %s not found", id));
    }

}
