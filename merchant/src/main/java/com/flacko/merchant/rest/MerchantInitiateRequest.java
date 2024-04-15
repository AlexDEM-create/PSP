package com.flacko.merchant.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public record MerchantInitiateRequest(@JsonProperty(ID) String id,
                                      @JsonProperty(NAME) Optional<String> name,
                                      @JsonProperty(USER_ID) Optional<String> userId) {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String USER_ID = "user_id";
}