package com.flacko.payment.verification.receipt.service.exception;

public class ReceiptPaymentVerificationInvalidCardLastFourDigitsException extends Exception {

    public ReceiptPaymentVerificationInvalidCardLastFourDigitsException(String id, String paymentId,
                                                                        String role, String invalidDigits) {
        super(String.format("Receipt payment verification %s failed for payment %s. " +
                        "The provided last four digits of the %s card number '%s' are invalid",
                id, paymentId, role, invalidDigits));
    }

}
