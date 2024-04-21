package com.flacko.payment.verification.sms.exception;

public class SmsPaymentVerificationInvalidCardLastFourDigitsException extends Exception {

    public SmsPaymentVerificationInvalidCardLastFourDigitsException(String id, String paymentId,
                                                                    String role, String invalidDigits) {
        super(String.format("SMS payment verification %s failed for payment %s. " +
                        "The provided last four digits of the %s card number '%s' are invalid",
                id, paymentId, role, invalidDigits));
    }

}
