package com.flacko.terminal.service;

import java.time.Instant;
import java.util.Optional;

public interface Terminal {

    Long getPrimaryKey();

    String getId();

    String getTraderTeamId();

    boolean isVerified();

    boolean isActive();

    Optional<String> getModel();

    Optional<String> getOperatingSystem();

    Instant getCreatedDate();

    Instant getUpdatedDate();

    Optional<Instant> getDeletedDate();

}
