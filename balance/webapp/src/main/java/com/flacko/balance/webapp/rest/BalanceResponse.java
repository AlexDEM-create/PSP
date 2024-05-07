package com.flacko.balance.webapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flacko.balance.service.EntityType;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record BalanceResponse(@JsonProperty(ID) String id,
                              @JsonProperty(ENTITY_ID) String entityId,
                              @JsonProperty(ENTITY_TYPE) EntityType entityType,
                              @JsonProperty(CURRENT_BALANCE) BigDecimal currentBalance,
                              @JsonProperty(CREATED_DATE) ZonedDateTime createdDate,
                              @JsonProperty(UPDATED_DATE) ZonedDateTime updatedDate) {

    private static final String ID = "id";
    private static final String ENTITY_ID = "entity_id";
    private static final String ENTITY_TYPE = "entity_type";
    private static final String CURRENT_BALANCE = "current_balance";
    private static final String CURRENCY = "currency";
    private static final String CREATED_DATE = "created_date";
    private static final String UPDATED_DATE = "updated_date";

}
