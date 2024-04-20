package com.flacko.merchant.exception;

import java.util.Optional;

public class MerchantMissingRequiredAttributeException extends Exception {

    public MerchantMissingRequiredAttributeException(String attributeName, Optional<String> id) {
        super(String.format("Missing required %s attribute for merchant %s", attributeName, id.orElse("unknown")));
    }

}
