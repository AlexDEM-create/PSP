package com.flacko.currency.impl;

import com.flacko.common.currency.Currency;
import com.flacko.common.id.IdGenerator;
import com.flacko.currency.service.CurrencyExchange;
import com.flacko.currency.service.CurrencyExchangeBuilder;
import com.flacko.currency.service.exception.CurrencyExchangeInvalidExchangeRateException;
import com.flacko.currency.service.exception.CurrencyExchangeMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CurrencyExchangeBuilderImpl implements InitializableCurrencyExchangeBuilder {

    private final Instant now = Instant.now();

    private final CurrencyExchangeRepository currencyExchangeRepository;

    private CurrencyExchangePojo.CurrencyExchangePojoBuilder pojoBuilder;

    @Override
    public CurrencyExchangeBuilder initializeNew() {
        pojoBuilder = CurrencyExchangePojo.builder()
                .id(new IdGenerator().generateId());
        return this;
    }

    @Override
    public CurrencyExchangeBuilder initializeExisting(CurrencyExchange existingCurrencyExchange) {
        pojoBuilder = CurrencyExchangePojo.builder()
                .primaryKey(existingCurrencyExchange.getPrimaryKey())
                .id(existingCurrencyExchange.getId())
                .sourceCurrency(existingCurrencyExchange.getSourceCurrency())
                .targetCurrency(existingCurrencyExchange.getTargetCurrency())
                .exchangeRate(existingCurrencyExchange.getExchangeRate())
                .updatedDate(now);
        return this;
    }

    @Override
    public CurrencyExchangeBuilder withSourceCurrency(Currency sourceCurrency) {
        pojoBuilder.sourceCurrency(sourceCurrency);
        return this;
    }

    @Override
    public CurrencyExchangeBuilder withTargetCurrency(Currency targetCurrency) {
        pojoBuilder.targetCurrency(targetCurrency);
        return this;
    }

    @Override
    public CurrencyExchangeBuilder withExchangeRate(BigDecimal exchangeRate) {
        pojoBuilder.exchangeRate(exchangeRate);
        return this;
    }


    @Override
    public CurrencyExchangePojo build() throws CurrencyExchangeMissingRequiredAttributeException,
            CurrencyExchangeInvalidExchangeRateException {
        CurrencyExchangePojo currency = pojoBuilder.build();
        validate(currency);
        currencyExchangeRepository.save(currency);
        return currency;
    }

    private void validate(CurrencyExchangePojo pojo) throws CurrencyExchangeMissingRequiredAttributeException,
            CurrencyExchangeInvalidExchangeRateException {
        if (pojo.getId() == null || pojo.getId().isBlank()) {
            throw new CurrencyExchangeMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getSourceCurrency() == null) {
            throw new CurrencyExchangeMissingRequiredAttributeException("sourceCurrency", Optional.of(pojo.getId()));
        }
        if (pojo.getTargetCurrency() == null) {
            throw new CurrencyExchangeMissingRequiredAttributeException("targetCurrency", Optional.of(pojo.getId()));
        }
        if (pojo.getExchangeRate() == null) {
            throw new CurrencyExchangeMissingRequiredAttributeException("exchangeRate", Optional.of(pojo.getId()));
        } else if (pojo.getExchangeRate().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CurrencyExchangeInvalidExchangeRateException(pojo.getExchangeRate(), pojo.getSourceCurrency(),
                    pojo.getTargetCurrency());
        }
    }

}
