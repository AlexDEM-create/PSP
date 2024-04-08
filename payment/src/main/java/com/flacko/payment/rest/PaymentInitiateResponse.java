package com.flacko.payment.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flacko.payment.PaymentState;

import java.time.ZonedDateTime;

public record PaymentInitiateResponse(@JsonProperty(CARD_NUMBER) String cardNumber) {

    private static final String CARD_NUMBER = "card_number";

}
