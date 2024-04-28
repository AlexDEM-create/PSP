package com.flacko.currency.impl;

import com.flacko.common.currency.Currency;
import com.flacko.common.exception.CurrencyExchangeNotFoundException;
import com.flacko.common.spring.ServiceLocator;
import com.flacko.currency.service.CurrencyExchange;
import com.flacko.currency.service.CurrencyExchangeBuilder;
import com.flacko.currency.service.CurrencyExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

    private final CurrencyExchangeRepository currencyExchangeRepository;
    private final ServiceLocator serviceLocator;

    @Override
    public List<CurrencyExchange> list() {
        return StreamSupport.stream(currencyExchangeRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public CurrencyExchange get(Currency sourceCurrency, Currency targetCurrency)
            throws CurrencyExchangeNotFoundException {
        return currencyExchangeRepository.findBySourceCurrencyAndTargetCurrency(sourceCurrency, targetCurrency)
                .orElseThrow(() -> new CurrencyExchangeNotFoundException(sourceCurrency, targetCurrency));
    }

    @Transactional
    @Override
    public CurrencyExchangeBuilder create() {
        return serviceLocator.create(InitializableCurrencyExchangeBuilder.class)
                .initializeNew();
    }

    @Transactional
    @Override
    public CurrencyExchangeBuilder update(Currency sourceCurrency, Currency targetCurrency)
            throws CurrencyExchangeNotFoundException {
        CurrencyExchange existingCurrencyExchange = get(sourceCurrency, targetCurrency);
        return serviceLocator.create(InitializableCurrencyExchangeBuilder.class)
                .initializeExisting(existingCurrencyExchange);
    }

}
