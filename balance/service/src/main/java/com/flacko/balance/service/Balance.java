package com.flacko.balance.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

public interface Balance {

    Long getPrimaryKey();

    String getId();

    String getEntityId();

    EntityType getEntityType();

    BigDecimal getCurrentBalance();

    Instant getCreatedDate();

    Instant getUpdatedDate();

    Optional<Instant> getDeletedDate();

}
