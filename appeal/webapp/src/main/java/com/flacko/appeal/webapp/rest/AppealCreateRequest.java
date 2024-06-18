package com.flacko.appeal.webapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public record AppealCreateRequest(@JsonProperty(PAYMENT_ID) String paymentId,
                                  @JsonProperty(MESSAGE) Optional<String> message) {

    private static final String PAYMENT_ID = "payment_id";
    private static final String MESSAGE = "message";

}
