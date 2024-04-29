package com.flacko.bank.service.exception;

import java.util.Optional;

public class BankMissingRequiredAttributeException extends Exception {

    public BankMissingRequiredAttributeException(String attributeName, Optional<String> id) {
        super(String.format("Missing required %s attribute for bank %s", attributeName, id.orElse("unknown")));
    }

}
