package com.flacko.currency.impl;

import com.flacko.currency.service.CurrencyExchange;
import com.flacko.currency.service.CurrencyExchangeBuilder;

public interface InitializableCurrencyExchangeBuilder extends CurrencyExchangeBuilder {

    CurrencyExchangeBuilder initializeNew();

    CurrencyExchangeBuilder initializeExisting(CurrencyExchange existingCurrencyExchange);

}
