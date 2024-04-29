package com.flacko.card.service;

import java.time.Instant;
import java.util.Optional;

public interface Card {

    Long getPrimaryKey();

    String getId();

    String getNumber();

    String getBankId();

    String getTraderTeamId();

    String getTerminalId();

    boolean isBusy();

    Instant getCreatedDate();

    Instant getUpdatedDate();

    Optional<Instant> getDeletedDate();

}
