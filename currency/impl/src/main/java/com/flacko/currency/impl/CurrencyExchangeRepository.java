package com.flacko.currency.impl;

import com.flacko.common.currency.Currency;
import com.flacko.currency.service.CurrencyExchange;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyExchangeRepository extends CrudRepository<CurrencyExchangePojo, Long> {

    @Query("SELECT c FROM CurrencyExchangePojo c WHERE c.id = :id")
    Optional<CurrencyExchange> findById(String id);

    @Query("SELECT c FROM CurrencyExchangePojo c WHERE c.sourceCurrency = :sourceCurrency " +
            "AND c.targetCurrency = :targetCurrency")
    Optional<CurrencyExchange> findBySourceCurrencyAndTargetCurrency(Currency sourceCurrency, Currency targetCurrency);

}
