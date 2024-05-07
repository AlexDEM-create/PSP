package com.flacko.incoming.payment.webapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IncomingPaymentInitiateResponse(@JsonProperty(CARD_NUMBER) String cardNumber) {

    private static final String CARD_NUMBER = "card_number";

}
