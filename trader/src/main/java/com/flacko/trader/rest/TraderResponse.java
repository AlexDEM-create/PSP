package com.flacko.trader.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TraderResponse(@JsonProperty(ID) String id,
                             @JsonProperty(USER_ID) String userId
) {
    private static final String ID = "id";
    private static final String USER_ID = "userId";
}