package com.flacko.currency.service;

import com.flacko.common.currency.Currency;
import com.flacko.common.exception.CurrencyExchangeNotFoundException;

import java.util.List;

public interface CurrencyExchangeService {

    CurrencyExchangeBuilder create();

    CurrencyExchangeBuilder update(Currency sourceCurrency, Currency targetCurrency)
            throws CurrencyExchangeNotFoundException;

    List<CurrencyExchange> list();

    CurrencyExchange get(Currency sourceCurrency, Currency targetCurrency) throws CurrencyExchangeNotFoundException;

}
