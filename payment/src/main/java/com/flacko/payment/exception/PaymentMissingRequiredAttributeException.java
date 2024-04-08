package com.flacko.payment.exception;

import java.util.Optional;

public class PaymentMissingRequiredAttributeException extends Exception {

    public PaymentMissingRequiredAttributeException(String attributeName, Optional<String> paymentId) {
        super("Missing required " + attributeName + " attribute for payment " + paymentId);
    }

}
