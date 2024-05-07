package com.flacko.payment.webapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flacko.common.currency.Currency;

import java.math.BigDecimal;

public record PaymentCreateRequest(@JsonProperty(REQUESTED_AMOUNT) BigDecimal requestedAmount,
                                   @JsonProperty(CURRENCY) Currency currency,
                                   @JsonProperty(PARTNER_CUSTOMER_ID) String partnerCustomerId) {

    private static final String REQUESTED_AMOUNT = "requested_amount";
    private static final String CURRENCY = "currency";
    private static final String PARTNER_CUSTOMER_ID = "partner_customer_id";

}
