package com.flacko.common.exception;

public class SmsPaymentVerificationNotFoundException extends NotFoundException {

    public SmsPaymentVerificationNotFoundException(String id) {
        super(String.format("SMS payment verification %s not found", id));
    }

}
