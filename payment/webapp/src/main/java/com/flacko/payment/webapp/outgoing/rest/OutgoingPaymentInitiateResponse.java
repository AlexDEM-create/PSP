package com.flacko.payment.webapp.outgoing.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OutgoingPaymentInitiateResponse(@JsonProperty(CARD_NUMBER) String cardNumber) {

    private static final String CARD_NUMBER = "card_number";

}
