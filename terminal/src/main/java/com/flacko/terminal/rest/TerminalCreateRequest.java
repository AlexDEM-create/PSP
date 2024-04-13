package com.flacko.terminal.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public record TerminalCreateRequest(@JsonProperty(TRADER_ID) String traderId,
                                    @JsonProperty(MODEL) Optional<String> model,
                                    @JsonProperty(OPERATING_SYSTEM) Optional<String> operatingSystem) {

    private static final String TRADER_ID = "trader_id";
    private static final String MODEL = "model";
    private static final String OPERATING_SYSTEM = "operating_system";

}
