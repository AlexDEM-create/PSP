package com.flacko.merchant.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record MerchantResponse(@JsonProperty(ID) String id,
                               @JsonProperty(NAME) String name,
                               @JsonProperty(USER_ID) String userId,
                               @JsonProperty(INCOMING_FEE_RATE) BigDecimal incomingFeeRate,
                               @JsonProperty(OUTGOING_FEE_RATE) BigDecimal outgoingFeeRate,
                               @JsonProperty(OUTGOING_TRAFFIC_STOPPED) boolean outgoingTrafficStopped,
                               @JsonProperty(CREATED_DATE) ZonedDateTime createdDate,
                               @JsonProperty(UPDATED_DATE) ZonedDateTime updatedDate) {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String USER_ID = "user_id";
    private static final String INCOMING_FEE_RATE = "incoming_fee_rate";
    private static final String OUTGOING_FEE_RATE = "outgoing_fee_rate";
    private static final String OUTGOING_TRAFFIC_STOPPED = "outgoing_traffic_stopped";
    private static final String CREATED_DATE = "created_date";
    private static final String UPDATED_DATE = "updated_date";

}
