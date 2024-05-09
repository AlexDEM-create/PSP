package com.flacko.payment.webapp.outgoing.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OutgoingPaymentCreateResponse(@JsonProperty(NUMBER) String number) {

    private static final String NUMBER = "number";

}
