package com.flacko.payment.verification.receipt.service.exception;

public class ReceiptPaymentVerificationFailedException extends Exception {

    public ReceiptPaymentVerificationFailedException(String outgoingPaymentId) {
        super(String.format("Outgoing payment %s verification failed", outgoingPaymentId));
    }

    public ReceiptPaymentVerificationFailedException(String outgoingPaymentId, Throwable e) {
        super(String.format("An error occurred during receipt verification for outgoing payment %s", outgoingPaymentId),
                e);
    }

}
