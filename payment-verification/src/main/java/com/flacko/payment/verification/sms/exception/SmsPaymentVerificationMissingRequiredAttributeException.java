package com.flacko.payment.verification.sms.exception;

import java.util.Optional;

public class SmsPaymentVerificationMissingRequiredAttributeException extends Exception {

    public SmsPaymentVerificationMissingRequiredAttributeException(String attributeName, Optional<String> id) {
        super(String.format("Missing required %s attribute for SMS payment verification %s", attributeName,
                id.orElse("unknown")));
    }

}
