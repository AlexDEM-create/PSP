package com.flacko.currency.service.exception;

import com.flacko.common.currency.Currency;

import java.math.BigDecimal;

public class CurrencyExchangeInvalidExchangeRateException extends Exception {

    public CurrencyExchangeInvalidExchangeRateException(String type, BigDecimal exchangeRate, Currency sourceCurrency,
                                                        Currency targetCurrency) {
        super(String.format("%s exchange rate %s should be more than 0 for source currency %s, target currency %s",
                type, exchangeRate, sourceCurrency, targetCurrency));
    }

}
