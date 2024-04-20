package com.flacko.card;

import java.time.Instant;
import java.util.Optional;

public interface Card {

    Long getPrimaryKey();

    String getId();

    String getNumber();

    String getBankId();

    String getTraderTeamId();

    boolean isBusy();

    Instant getCreatedDate();

    Instant getUpdatedDate();

    Optional<Instant> getDeletedDate();

}
