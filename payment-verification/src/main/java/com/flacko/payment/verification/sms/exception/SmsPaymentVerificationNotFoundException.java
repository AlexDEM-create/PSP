package com.flacko.payment.verification.sms.exception;

import com.flacko.auth.exception.NotFoundException;

public class SmsPaymentVerificationNotFoundException extends NotFoundException {

    public SmsPaymentVerificationNotFoundException(String id) {
        super(String.format("SMS payment verification %s not found", id));
    }

}
