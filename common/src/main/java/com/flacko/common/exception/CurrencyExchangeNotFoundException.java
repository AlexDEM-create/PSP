package com.flacko.common.exception;

import com.flacko.common.currency.Currency;

public class CurrencyExchangeNotFoundException extends NotFoundException {

    public CurrencyExchangeNotFoundException(Currency sourceCurrency, Currency targetCurrency) {
        super(String.format("Currency exchange %s-%s not found", sourceCurrency, targetCurrency));
    }

}
