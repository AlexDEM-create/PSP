package com.flacko.merchant;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

public interface Merchant {

    Long getPrimaryKey();

    String getId();

    String getName();

    String getUserId();

    BigDecimal getIncomingFeeRate();

    BigDecimal getOutgoingFeeRate();

    boolean isOutgoingTrafficStopped();

    Instant getCreatedDate();

    Instant getUpdatedDate();

    Optional<Instant> getDeletedDate();

}
