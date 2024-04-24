package com.flacko.currency.exception;

import com.flacko.auth.exception.NotFoundException;

public class CurrencyNotFoundException extends NotFoundException {

    public CurrencyNotFoundException(String id) {
        super(String.format("Currency %s not found", id));
    }

}
