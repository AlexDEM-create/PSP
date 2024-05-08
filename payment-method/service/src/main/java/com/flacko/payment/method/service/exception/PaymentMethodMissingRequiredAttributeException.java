package com.flacko.payment.method.service.exception;

import java.util.Optional;

public class PaymentMethodMissingRequiredAttributeException extends Exception {

    public PaymentMethodMissingRequiredAttributeException(String attributeName, Optional<String> id) {
        super(String.format("Missing required %s attribute for payment method %s", attributeName,
                id.orElse("unknown")));
    }

}
