package com.flacko.bank;

import java.time.Instant;
import java.util.Optional;

public interface Bank {

    Long getPrimaryKey();

    String getId();

    String getName();

    String getCountry();

    Instant getCreatedDate();

    Instant getUpdatedDate();

    Optional<Instant> getDeletedDate();

}
