package com.flacko.trader.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TraderInitiateRequest(@JsonProperty(NAME) String name) {
    private static final String NAME = "name";
}
