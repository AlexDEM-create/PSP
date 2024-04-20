package com.flacko.trader.team;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

public interface TraderTeam {

    Long getPrimaryKey();

    String getId();

    String getName();

    String getUserId();

    String getLeaderId();

    BigDecimal getTraderIncomingFeeRate();

    BigDecimal getTraderOutgoingFeeRate();

    BigDecimal getLeaderIncomingFeeRate();

    BigDecimal getLeaderOutgoingFeeRate();

    boolean isKickedOut();

    Instant getCreatedDate();

    Instant getUpdatedDate();

    Optional<Instant> getDeletedDate();

}
