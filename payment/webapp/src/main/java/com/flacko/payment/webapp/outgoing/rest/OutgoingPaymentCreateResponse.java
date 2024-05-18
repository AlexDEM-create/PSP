package com.flacko.payment.webapp.outgoing.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OutgoingPaymentCreateResponse(@JsonProperty(ID) String id) {

    private static final String ID = "id";

}
