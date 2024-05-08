package com.flacko.balance.service;

import com.flacko.common.currency.Currency;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

public interface Balance {

    Long getPrimaryKey();

    String getId();

    String getEntityId();

    EntityType getEntityType();

    BigDecimal getCurrentBalance();

    Currency getCurrency();

    Instant getCreatedDate();

    Instant getUpdatedDate();

    Optional<Instant> getDeletedDate();

}
