package com.flacko.bank.service;

import com.flacko.common.country.Country;

import java.time.Instant;
import java.util.Optional;

public interface Bank {

    Long getPrimaryKey();

    String getId();

    String getName();

    Country getCountry();

    Instant getCreatedDate();

    Instant getUpdatedDate();

    Optional<Instant> getDeletedDate();

}
