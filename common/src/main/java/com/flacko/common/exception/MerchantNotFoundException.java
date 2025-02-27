package com.flacko.common.exception;

public class MerchantNotFoundException extends NotFoundException {

    public MerchantNotFoundException(String id) {
        super(String.format("Merchant %s not found", id));
    }

    public MerchantNotFoundException(String entity, String entityId) {
        super(String.format("Merchant not found by %s %s", entity, entityId));
    }

}
