package com.flacko.currency;

import java.time.Instant;
import java.util.Currency;

import com.flacko.auth.id.IdGenerator;
import com.flacko.currency.exception.CurrencyInvalidRateException;
import com.flacko.currency.exception.CurrencyMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CurrencyBuilderImpl implements InitializableCurrencyBuilder {

    private final Instant now = Instant.now();

    private final CurrencyRepository currencyRepository;

    private CurrencyPojo.CurrencyPojoBuilder pojoBuilder;

    @Override
    public CurrencyBuilder initializeNew() {
        pojoBuilder = CurrencyPojo.builder()
                .id(new IdGenerator().generateId());
        return this;
    }

    @Override
    public CurrencyBuilder initializeExisting(Currency existingCurrency) {
        pojoBuilder = CurrencyPojo.builder()
                .primaryKey(existingCurrency.getPrimaryKey())
                .id(existingCurrency.getId())
                .tradeType(existingCurrency.getTradeType())
                .rate(existingCurrency.getRate())
                .fiat(existingCurrency.getFiat());
        return this;
    }

    @Override
    public CurrencyBuilder withTradeType(String tradeType) {
        pojoBuilder.tradeType(tradeType);
        return this;
    }

    @Override
    public CurrencyBuilder withRate(String rate) {
        pojoBuilder.rate(rate);
        return this;
    }

    @Override
    public CurrencyBuilder withFiat(String fiat) {
        pojoBuilder.fiat(fiat);
        return this;
    }


    @Override
    public CurrencyPojo build() throws CurrencyMissingRequiredAttributeException, CurrencyInvalidRateException {
        CurrencyPojo currency = pojoBuilder.build();
        validate(currency);
        currencyRepository.save(currency);
        return currency;
    }

    private void validate(CurrencyPojo pojo) throws CurrencyMissingRequiredAttributeException, CurrencyInvalidRateException {
        // Add validation logic here
    }

}
