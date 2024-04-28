package com.flacko.merchant.webapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MerchantInitiateSettlementResponse(@JsonProperty(SETTLEMENT_ID) String settlementId) {
    private static final String SETTLEMENT_ID = "settlement_id";
}
