package com.flacko.card.rest;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public record CardResponse(@JsonProperty(ID) String cardId,
                           @JsonProperty(NAME) String cardName,
                           @JsonProperty(NUMBER) String cardNumber,
                           @JsonProperty(CREATED_DATE) ZonedDateTime createdDate,
                           @JsonProperty(UPDATED_DATE) ZonedDateTime updatedDate) {

    private static final String ID = "cardId";
    private static final String NAME = "cardName";
    private static final String NUMBER = "cardNumber";
    private static final String CREATED_DATE = "created_date";
    private static final String UPDATED_DATE = "updated_date";
}