package com.flacko.merchant.rest;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record MerchantResponse(@JsonProperty(ID) String id,
                               @JsonProperty(NAME) String name,
                               @JsonProperty(CREATED_DATE) Instant createdDate,
                               @JsonProperty(UPDATED_DATE) Instant updatedDate) {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String CREATED_DATE = "created_date";
    private static final String UPDATED_DATE = "updated_date";
}
