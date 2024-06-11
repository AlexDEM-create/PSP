package com.flacko.terminal.webapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TerminalCreateRequest(@JsonProperty(NAME) String name,
                                    @JsonProperty(TRADER_TEAM_ID) String traderTeamId) {

    private static final String NAME = "name";
    private static final String TRADER_TEAM_ID = "trader_team_id";

}
