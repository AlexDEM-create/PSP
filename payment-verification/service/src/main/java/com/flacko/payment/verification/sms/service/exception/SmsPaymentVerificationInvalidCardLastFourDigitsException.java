package com.flacko.payment.verification.sms.service.exception;

public class SmsPaymentVerificationInvalidCardLastFourDigitsException extends Exception {

    public SmsPaymentVerificationInvalidCardLastFourDigitsException(String id, String incomingPaymentId, String role,
                                                                    String invalidDigits) {
        super(String.format("SMS payment verification %s failed for incoming payment %s. " +
                        "The provided last four digits of the %s card number '%s' are invalid",
                id, incomingPaymentId, role, invalidDigits));
    }

}
