package com.flacko.merchant.service;

import com.flacko.common.country.Country;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

public interface Merchant {

    Long getPrimaryKey();

    String getId();

    String getName();

    String getUserId();

    Country getCountry();

    BigDecimal getIncomingFeeRate();

    BigDecimal getOutgoingFeeRate();

    boolean isOutgoingTrafficStopped();

    Instant getCreatedDate();

    Instant getUpdatedDate();

    Optional<Instant> getDeletedDate();

}
