package com.flacko.payment.verification.receipt.exception;

import java.util.Optional;

public class ReceiptPaymentVerificationMissingRequiredAttributeException extends Exception {

    public ReceiptPaymentVerificationMissingRequiredAttributeException(String attributeName,
                                                                       Optional<String> receiptPaymentVerificationId) {
        super("Missing required " + attributeName + " attribute for receipt payment verification "
                + receiptPaymentVerificationId);
    }

}
