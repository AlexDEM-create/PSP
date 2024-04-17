package com.flacko.payment.verification.sms.exception;

public class SmsPaymentVerificationNotFoundException extends Exception {

    public SmsPaymentVerificationNotFoundException(String id) {
        super(String.format("SMS payment verification %s not found", id));
    }

}
