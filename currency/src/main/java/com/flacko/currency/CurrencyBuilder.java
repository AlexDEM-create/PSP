package com.flacko.currency;

public interface CurrencyBuilder {

    CurrencyBuilder withTradeType(String tradeType);

    CurrencyBuilder withRate(String rate);

    CurrencyBuilder withFiat(String fiat);

    CurrencyPojo build() throws com.flacko.currency.exception.CurrencyMissingRequiredAttributeException, com.flacko.currency.exception.CurrencyInvalidRateException;
}