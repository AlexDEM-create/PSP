package com.flacko.payment.webapp.outgoing.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flacko.common.currency.Currency;

import java.math.BigDecimal;

public record OutgoingPaymentCreateRequest(@JsonProperty(AMOUNT) BigDecimal amount,
                                             @JsonProperty(CURRENCY) Currency currency,
                                             @JsonProperty(PARTNER_PAYMENT_ID) String partnerPaymentId,
                                             @JsonProperty(PARTNER_CUSTOMER_ID) String partnerCustomerId) {

    private static final String AMOUNT = "amount";
    private static final String CURRENCY = "currency";
    private static final String PARTNER_PAYMENT_ID = "partner_payment_id";
    private static final String PARTNER_CUSTOMER_ID = "partner_customer_id";

}
