package com.flacko.trader.team.service;

import com.flacko.common.country.Country;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

public interface TraderTeam {

    Long getPrimaryKey();

    String getId();

    String getName();

    String getUserId();

    Country getCountry();

    String getLeaderId();

    BigDecimal getTraderIncomingFeeRate();

    BigDecimal getTraderOutgoingFeeRate();

    BigDecimal getLeaderIncomingFeeRate();

    BigDecimal getLeaderOutgoingFeeRate();

    boolean isOnline();

    boolean isKickedOut();

    Instant getCreatedDate();

    Instant getUpdatedDate();

    Optional<Instant> getDeletedDate();

}
