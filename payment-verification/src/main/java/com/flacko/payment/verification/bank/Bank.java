package com.flacko.payment.verification.bank;

import java.time.Instant;
import java.util.Optional;

public interface Bank {

    String getId();

    String getName();

    String getCountry();

    Instant getCreatedDate();

    Instant getUpdatedDate();

    Optional<Instant> getDeletedDate();

}
