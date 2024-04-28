package com.flacko.common.exception;

public class ReceiptPaymentVerificationNotFoundException extends NotFoundException {

    public ReceiptPaymentVerificationNotFoundException(String id) {
        super(String.format("Receipt payment verification %s not found", id));
    }

}
