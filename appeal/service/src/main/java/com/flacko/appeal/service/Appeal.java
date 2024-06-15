package com.flacko.appeal.service;

import java.time.Instant;
import java.util.Optional;

public interface Appeal {

    Long getPrimaryKey();

    String getId();

    String getPaymentId();

    PaymentDirection getPaymentDirection();

    AppealSource getSource();

    AppealState getCurrentState();

    Optional<String> getMessage();

    Instant getCreatedDate();

    Instant getUpdatedDate();

}
