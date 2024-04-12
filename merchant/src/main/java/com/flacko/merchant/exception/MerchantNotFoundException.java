package com.flacko.merchant.exception;

public class MerchantNotFoundException extends Exception {
    public MerchantNotFoundException(String id) {
        super(String.format("Merchant %s not found", id));
    }
}