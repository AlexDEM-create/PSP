package com.flacko.payment.exception;

import com.flacko.auth.exception.NotFoundException;

public class PaymentNotFoundException extends NotFoundException {

    public PaymentNotFoundException(String id) {
        super(String.format("Payment %s not found", id));
    }

}
