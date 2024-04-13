package com.flacko.terminal.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.Optional;

public record TerminalResponse(@JsonProperty(ID) String id,
                               @JsonProperty(TRADER_ID) String traderId,
                               @JsonProperty(MODEL) Optional<String> model,
                               @JsonProperty(OPERATING_SYSTEM) Optional<String> operatingSystem,
                               @JsonProperty(CREATED_DATE) ZonedDateTime createdDate,
                               @JsonProperty(UPDATED_DATE) ZonedDateTime updatedDate) {

    private static final String ID = "id";
    private static final String TRADER_ID = "trader_id";
    private static final String MODEL = "model";
    private static final String OPERATING_SYSTEM = "operating_system";
    private static final String CREATED_DATE = "created_date";
    private static final String UPDATED_DATE = "updated_date";

}
