package com.flacko.card.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CardCreateRequest(@JsonProperty(NUMBER) String number,
                                @JsonProperty(BANK_ID) String bankId,
                                @JsonProperty(TRADER_TEAM_ID) String traderTeamId) {

    private static final String NUMBER = "number";
    private static final String BANK_ID = "bank_id";
    private static final String TRADER_TEAM_ID = "trader_team_id";

}
