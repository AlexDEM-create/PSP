package com.flacko.payment.verification.receipt.exception;

public class ReceiptPaymentVerificationRequestValidationException extends Exception {

    public ReceiptPaymentVerificationRequestValidationException(String message) {
        super(message);
    }

    public ReceiptPaymentVerificationRequestValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
