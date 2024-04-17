package com.flacko.payment.verification.receipt.exception;

public class ReceiptPaymentVerificationValidationException extends Exception {

    public ReceiptPaymentVerificationValidationException(String message) {
        super(message);
    }

    public ReceiptPaymentVerificationValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
