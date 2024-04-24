package com.flacko.currency;

import com.flacko.currency.exception.CurrencyNotFoundException;

import java.util.List;

public interface CurrencyService {

    CurrencyBuilder create();

    CurrencyBuilder update(String id) throws CurrencyNotFoundException;

    List<Currency> list();

    Currency get(String id) throws CurrencyNotFoundException;

}
