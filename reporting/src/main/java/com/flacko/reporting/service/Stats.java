package com.flacko.reporting.service;


import java.math.BigDecimal;
import java.time.Instant;

public interface Stats {

    Long getPrimaryKey();

    String getId();

    String getEntityId();

    EntityType getEntityType();

    BigDecimal getTodayOutgoingTotal();

    BigDecimal getTodayIncomingTotal();

    BigDecimal getAllTimeOutgoingTotal();

    BigDecimal getAllTimeIncomingTotal();

    Instant getCreatedDate();

    Instant getUpdatedDate();

}