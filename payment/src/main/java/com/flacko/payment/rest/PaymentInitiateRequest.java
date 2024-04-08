package com.flacko.payment.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Currency;

public record PaymentInitiateRequest(@JsonProperty(REQUESTED_AMOUNT) BigDecimal requestedAmount,
                                     @JsonProperty(CURRENCY) Currency currency) {

    private static final String REQUESTED_AMOUNT = "requested_amount";
    private static final String CURRENCY = "currency";

}
