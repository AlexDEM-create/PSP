package com.flacko.payment.exception;

import java.util.Optional;

public class PaymentMissingRequiredAttributeException extends Exception {

    public PaymentMissingRequiredAttributeException(String attributeName, Optional<String> id) {
        super(String.format("Missing required %s attribute for payment %s", attributeName, id.orElse("unknown")));
    }

}
