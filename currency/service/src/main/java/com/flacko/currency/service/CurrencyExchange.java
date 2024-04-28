package com.flacko.currency.service;

import com.flacko.common.currency.Currency;

import java.math.BigDecimal;

public interface CurrencyExchange {

    Long getPrimaryKey();

    String getId();

    Currency getSourceCurrency();

    Currency getTargetCurrency();

    BigDecimal getExchangeRate();

}