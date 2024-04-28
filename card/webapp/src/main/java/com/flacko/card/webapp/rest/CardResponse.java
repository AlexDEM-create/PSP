package com.flacko.card.webapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public record CardResponse(@JsonProperty(ID) String id,
                           @JsonProperty(NUMBER) String number,
                           @JsonProperty(BANK_ID) String bankId,
                           @JsonProperty(TRADER_TEAM_ID) String traderTeamId,
                           @JsonProperty(ACTIVE) boolean active,
                           @JsonProperty(CREATED_DATE) ZonedDateTime createdDate,
                           @JsonProperty(UPDATED_DATE) ZonedDateTime updatedDate) {

    private static final String ID = "id";
    private static final String NUMBER = "number";
    private static final String BANK_ID = "bank_id";
    private static final String TRADER_TEAM_ID = "trader_team_id";
    private static final String ACTIVE = "active";
    private static final String CREATED_DATE = "created_date";
    private static final String UPDATED_DATE = "updated_date";

}
