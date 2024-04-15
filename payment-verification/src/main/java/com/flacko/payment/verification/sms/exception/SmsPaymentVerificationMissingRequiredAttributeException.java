package com.flacko.payment.verification.sms.exception;

import java.util.Optional;

public class SmsPaymentVerificationMissingRequiredAttributeException extends Exception {

    public SmsPaymentVerificationMissingRequiredAttributeException(String attributeName,
                                                                   Optional<String> smsPaymentVerificationId) {
        super("Missing required " + attributeName + " attribute for SMS payment verification "
                + smsPaymentVerificationId);
    }

}
