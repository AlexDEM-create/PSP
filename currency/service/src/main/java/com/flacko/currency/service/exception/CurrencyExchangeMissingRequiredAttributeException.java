package com.flacko.currency.service.exception;

import java.util.Optional;

public class CurrencyExchangeMissingRequiredAttributeException extends Exception {

    public CurrencyExchangeMissingRequiredAttributeException(String attributeName, Optional<String> id) {
        super(String.format("Missing required %s attribute for currency %s", attributeName, id.orElse("unknown")));
    }

}
