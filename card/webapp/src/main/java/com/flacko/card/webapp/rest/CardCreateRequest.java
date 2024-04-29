package com.flacko.card.webapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CardCreateRequest(@JsonProperty(NUMBER) String number,
                                @JsonProperty(BANK_ID) String bankId,
                                @JsonProperty(TRADER_TEAM_ID) String traderTeamId,
                                @JsonProperty(TERMINAL_ID) String terminalId) {

    private static final String NUMBER = "number";
    private static final String BANK_ID = "bank_id";
    private static final String TRADER_TEAM_ID = "trader_team_id";
    private static final String TERMINAL_ID = "terminal_id";

}
