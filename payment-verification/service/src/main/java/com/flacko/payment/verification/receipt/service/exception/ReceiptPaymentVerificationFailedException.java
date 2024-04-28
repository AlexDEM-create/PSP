package com.flacko.payment.verification.receipt.service.exception;

public class ReceiptPaymentVerificationFailedException extends Exception {

    public ReceiptPaymentVerificationFailedException(String paymentId) {
        super(String.format("Payment %s verification failed", paymentId));
    }

    public ReceiptPaymentVerificationFailedException(String paymentId, Throwable e) {
        super(String.format("An error occurred during receipt verification for payment %s", paymentId), e);
    }

}
