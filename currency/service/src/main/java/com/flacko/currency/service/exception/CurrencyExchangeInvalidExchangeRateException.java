package com.flacko.currency.service.exception;

import com.flacko.common.currency.Currency;

import java.math.BigDecimal;

public class CurrencyExchangeInvalidExchangeRateException extends Exception {

    public CurrencyExchangeInvalidExchangeRateException(BigDecimal exchangeRate, Currency sourceCurrency,
                                                        Currency targetCurrency) {
        super(String.format("Exchange rate %s should be more than 0 for source currency %s, target currency %s",
                exchangeRate, sourceCurrency, targetCurrency));
    }

}
