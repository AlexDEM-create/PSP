package com.flacko.payment.verification.receipt.exception;

import com.flacko.auth.exception.NotFoundException;

public class ReceiptPaymentVerificationNotFoundException extends NotFoundException {

    public ReceiptPaymentVerificationNotFoundException(String id) {
        super(String.format("Receipt payment verification %s not found", id));
    }

}
