package com.flacko.appeal.webapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AppealCreateRequest(@JsonProperty(PAYMENT_ID) String paymentId) {

    private static final String PAYMENT_ID = "payment_id";

}
