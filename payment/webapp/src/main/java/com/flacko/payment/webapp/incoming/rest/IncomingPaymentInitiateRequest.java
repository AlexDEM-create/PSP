package com.flacko.payment.webapp.incoming.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flacko.common.currency.Currency;

import java.math.BigDecimal;

public record IncomingPaymentInitiateRequest(@JsonProperty(REQUESTED_AMOUNT) BigDecimal requestedAmount,
                                             @JsonProperty(CURRENCY) Currency currency) {

    private static final String REQUESTED_AMOUNT = "requested_amount";
    private static final String CURRENCY = "currency";

}
