package com.flacko.currency.exception;

import java.util.Optional;

public class CurrencyMissingRequiredAttributeException extends Exception {

    public CurrencyMissingRequiredAttributeException(String attributeName, Optional<String> id) {
        super(String.format("Missing required %s attribute for currency %s", attributeName, id.orElse("unknown")));
    }

}