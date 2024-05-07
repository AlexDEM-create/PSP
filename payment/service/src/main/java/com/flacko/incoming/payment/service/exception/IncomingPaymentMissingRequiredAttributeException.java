package com.flacko.incoming.payment.service.exception;

import java.util.Optional;

public class IncomingPaymentMissingRequiredAttributeException extends Exception {

    public IncomingPaymentMissingRequiredAttributeException(String attributeName, Optional<String> id) {
        super(String.format("Missing required %s attribute for payment %s", attributeName, id.orElse("unknown")));
    }

}
