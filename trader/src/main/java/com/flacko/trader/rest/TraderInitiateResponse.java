package com.flacko.trader.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TraderInitiateResponse(@JsonProperty(ID) String id,
                                     @JsonProperty(NAME) String name) {
    private static final String ID = "id";
    private static final String NAME = "name";
}
