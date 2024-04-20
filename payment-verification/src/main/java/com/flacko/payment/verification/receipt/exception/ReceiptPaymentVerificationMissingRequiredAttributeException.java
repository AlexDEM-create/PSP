package com.flacko.payment.verification.receipt.exception;

import java.util.Optional;

public class ReceiptPaymentVerificationMissingRequiredAttributeException extends Exception {

    public ReceiptPaymentVerificationMissingRequiredAttributeException(String attributeName, Optional<String> id) {
        super(String.format("Missing required %s attribute for receipt payment verification %s", attributeName,
                id.orElse("unknown")));
    }

}
