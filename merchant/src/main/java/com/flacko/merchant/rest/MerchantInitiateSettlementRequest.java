package com.flacko.merchant.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record MerchantInitiateSettlementRequest(@JsonProperty(REQUESTED_AMOUNT) BigDecimal requestedAmount) {
    private static final String REQUESTED_AMOUNT = "requested_amount";
}
