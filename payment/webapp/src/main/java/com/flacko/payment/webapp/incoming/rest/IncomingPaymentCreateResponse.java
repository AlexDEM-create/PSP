package com.flacko.payment.webapp.incoming.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IncomingPaymentCreateResponse(@JsonProperty(NUMBER) String number) {

    private static final String NUMBER = "number";

}
