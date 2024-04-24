package com.flacko.currency;

import com.flacko.currency.CurrencyBuilder;

import java.util.Currency;

public interface InitializableCurrencyBuilder extends CurrencyBuilder {

    CurrencyBuilder initializeNew();

    CurrencyBuilder initializeExisting(Currency existingCurrency);
}