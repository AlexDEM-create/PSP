package com.flacko.outgoing.payment.webapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OutgoingPaymentInitiateResponse(@JsonProperty(CARD_NUMBER) String cardNumber) {

    private static final String CARD_NUMBER = "card_number";

}
