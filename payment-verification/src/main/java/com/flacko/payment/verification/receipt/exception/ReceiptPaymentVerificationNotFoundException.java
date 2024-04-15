package com.flacko.payment.verification.receipt.exception;

public class ReceiptPaymentVerificationNotFoundException extends Exception {

    public ReceiptPaymentVerificationNotFoundException(String id) {
        super(String.format("Receipt payment verification %s not found", id));
    }

}
