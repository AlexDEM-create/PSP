package com.flacko.trader.TraderTeam.rest;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TraderTeamResponse(@JsonProperty(ID) String id,
                                 @JsonProperty(NAME) String name,
                                 @JsonProperty(IS_KICKED_OUT) Boolean isKickedOut
) {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String IS_KICKED_OUT = "isKickedOut";
}
