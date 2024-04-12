package com.flacko.merchant.exception;

import java.util.Optional;

public class MerchantMissingRequiredAttributeException extends Exception {
    public MerchantMissingRequiredAttributeException(String attributeName, Optional<String> id) {
        super("Missing required " + attributeName + " attribute for merchant " + id.orElse("unknown"));
    }
}