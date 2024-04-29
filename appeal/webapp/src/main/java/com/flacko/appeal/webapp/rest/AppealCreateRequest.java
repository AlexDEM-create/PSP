package com.flacko.appeal.webapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flacko.appeal.service.AppealSource;

public record AppealCreateRequest(@JsonProperty(PAYMENT_ID) String paymentId,
                                  @JsonProperty(SOURCE) AppealSource source) {

    private static final String PAYMENT_ID = "payment_id";
    private static final String SOURCE = "source";

}
