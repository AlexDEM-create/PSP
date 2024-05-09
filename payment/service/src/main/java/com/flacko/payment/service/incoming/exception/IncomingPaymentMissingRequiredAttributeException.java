package com.flacko.payment.service.incoming.exception;

import java.util.Optional;

public class IncomingPaymentMissingRequiredAttributeException extends Exception {

    public IncomingPaymentMissingRequiredAttributeException(String attributeName, Optional<String> id) {
        super(String.format("Missing required %s attribute for incoming payment %s", attributeName,
                id.orElse("unknown")));
    }

}
