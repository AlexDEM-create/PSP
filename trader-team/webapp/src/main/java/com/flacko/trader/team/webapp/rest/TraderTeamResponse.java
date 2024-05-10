package com.flacko.trader.team.webapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flacko.common.country.Country;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record TraderTeamResponse(@JsonProperty(ID) String id,
                                 @JsonProperty(NAME) String name,
                                 @JsonProperty(USER_ID) String userId,
                                 @JsonProperty(COUNTRY) Country country,
                                 @JsonProperty(LEADER_ID) String leaderId,
                                 @JsonProperty(TRADER_INCOMING_FEE_RATE) BigDecimal traderIncomingFeeRate,
                                 @JsonProperty(TRADER_OUTGOING_FEE_RATE) BigDecimal traderOutgoingFeeRate,
                                 @JsonProperty(LEADER_INCOMING_FEE_RATE) BigDecimal leaderIncomingFeeRate,
                                 @JsonProperty(LEADER_OUTGOING_FEE_RATE) BigDecimal leaderOutgoingFeeRate,
                                 @JsonProperty(ONLINE) boolean online,
                                 @JsonProperty(KICKED_OUT) boolean kickedOut,
                                 @JsonProperty(CREATED_DATE) ZonedDateTime createdDate,
                                 @JsonProperty(UPDATED_DATE) ZonedDateTime updatedDate) {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String USER_ID = "user_id";
    private static final String COUNTRY = "country";
    private static final String LEADER_ID = "leader_id";
    private static final String TRADER_INCOMING_FEE_RATE = "trader_incoming_fee_rate";
    private static final String TRADER_OUTGOING_FEE_RATE = "trader_outgoing_fee_rate";
    private static final String LEADER_INCOMING_FEE_RATE = "leader_incoming_fee_rate";
    private static final String LEADER_OUTGOING_FEE_RATE = "leader_outgoing_fee_rate";
    private static final String ONLINE = "online";
    private static final String KICKED_OUT = "kicked_out";
    private static final String CREATED_DATE = "created_date";
    private static final String UPDATED_DATE = "updated_date";

}
