package com.flacko.merchant.webapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record MerchantCreateRequest(@JsonProperty(NAME) String name,
                                    @JsonProperty(USER_ID) String userId,
                                    @JsonProperty(INCOMING_FEE_RATE) BigDecimal incomingFeeRate,
                                    @JsonProperty(OUTGOING_FEE_RATE) BigDecimal outgoingFeeRate) {

    private static final String NAME = "name";
    private static final String USER_ID = "user_id";
    private static final String INCOMING_FEE_RATE = "incoming_fee_rate";
    private static final String OUTGOING_FEE_RATE = "outgoing_fee_rate";

}
