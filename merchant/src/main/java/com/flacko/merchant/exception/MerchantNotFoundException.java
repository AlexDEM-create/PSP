package com.flacko.merchant.exception;

import com.flacko.auth.exception.NotFoundException;

public class MerchantNotFoundException extends NotFoundException {

    public MerchantNotFoundException(String id) {
        super(String.format("Merchant %s not found", id));
    }

}
