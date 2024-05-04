package com.flacko.currency.service;

import com.flacko.common.currency.Currency;
import com.flacko.currency.service.exception.CurrencyExchangeInvalidExchangeRateException;
import com.flacko.currency.service.exception.CurrencyExchangeMissingRequiredAttributeException;

import java.math.BigDecimal;

public interface CurrencyExchangeBuilder {

    CurrencyExchangeBuilder withSourceCurrency(Currency sourceCurrency);

    CurrencyExchangeBuilder withTargetCurrency(Currency targetCurrency);

    CurrencyExchangeBuilder withBuyExchangeRate(BigDecimal buyExchangeRate);

    CurrencyExchangeBuilder withSellExchangeRate(BigDecimal sellExchangeRate);

    CurrencyExchange build() throws CurrencyExchangeMissingRequiredAttributeException,
            CurrencyExchangeInvalidExchangeRateException;

}
