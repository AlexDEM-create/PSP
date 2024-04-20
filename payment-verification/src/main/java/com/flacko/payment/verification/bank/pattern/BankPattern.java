package com.flacko.payment.verification.bank.pattern;

import java.time.Instant;
import java.util.Optional;

public interface BankPattern {

    Long getPrimaryKey();

    String getId();

    String getPattern();

    BankPatternType getType();

    // add foreign key to banks table
    String getBankId();

    Instant getCreatedDate();

    Instant getUpdatedDate();

    Optional<Instant> getDeletedDate();

}
