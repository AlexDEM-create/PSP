package com.flacko.common.exception;

import java.util.Optional;

public class OutgoingPaymentMissingRequiredAttributeException extends Exception {

    public OutgoingPaymentMissingRequiredAttributeException(String attributeName, Optional<String> id) {
        super(String.format("Missing required %s attribute for outgoing payment %s", attributeName,
                id.orElse("unknown")));
    }

}
