package com.flacko.appeal.service;

import java.time.Instant;

public interface Appeal {

    Long getPrimaryKey();

    String getId();

    String getPaymentId();

    AppealState getCurrentState();

    Instant getCreatedDate();

    Instant getUpdatedDate();

}
