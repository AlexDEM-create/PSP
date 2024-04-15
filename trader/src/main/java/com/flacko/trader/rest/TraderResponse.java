package com.flacko.trader.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import static javax.swing.Action.NAME;
import static org.springframework.data.jpa.domain.AbstractPersistable_.ID;

public record TraderResponse(@JsonProperty(ID) String id,
                             @JsonProperty(NAME) String name,
                             @JsonProperty(USER_ID) String userId) {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String USER_ID = "userId";
}