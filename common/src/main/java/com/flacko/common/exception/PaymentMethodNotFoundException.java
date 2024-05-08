package com.flacko.common.exception;

public class PaymentMethodNotFoundException extends NotFoundException {

    public PaymentMethodNotFoundException(String id) {
        super(String.format("Payment method %s not found", id));
    }

}
