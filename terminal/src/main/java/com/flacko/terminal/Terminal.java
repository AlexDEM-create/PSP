package com.flacko.terminal;

import java.time.Instant;
import java.util.Optional;

public interface Terminal {

    Long getPrimaryKey();

    String getId();

    String getTraderId();

    boolean isVerified();

    Optional<String> getModel();

    Optional<String> getOperatingSystem();

    Instant getCreatedDate();

    Instant getUpdatedDate();

    Optional<Instant> getDeletedDate();

}
