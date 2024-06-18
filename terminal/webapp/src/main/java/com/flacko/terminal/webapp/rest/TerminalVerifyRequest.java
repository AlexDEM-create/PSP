package com.flacko.terminal.webapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public record TerminalVerifyRequest(@JsonProperty(MODEL) Optional<String> model,
                                    @JsonProperty(OPERATING_SYSTEM) Optional<String> operatingSystem) {

    private static final String MODEL = "model";
    private static final String OPERATING_SYSTEM = "operating_system";

}
