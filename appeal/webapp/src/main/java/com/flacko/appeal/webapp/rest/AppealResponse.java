package com.flacko.appeal.webapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flacko.appeal.service.AppealSource;
import com.flacko.appeal.service.AppealState;

import java.time.ZonedDateTime;

public record AppealResponse(@JsonProperty(ID) String id,
                             @JsonProperty(PAYMENT_ID) String paymentId,
                             @JsonProperty(SOURCE) AppealSource source,
                             @JsonProperty(CURRENT_STATE) AppealState currentState,
                             @JsonProperty(CREATED_DATE) ZonedDateTime createdDate,
                             @JsonProperty(UPDATED_DATE) ZonedDateTime updatedDate) {

    private static final String ID = "id";
    private static final String PAYMENT_ID = "payment_id";
    private static final String SOURCE = "source";
    private static final String CURRENT_STATE = "current_state";
    private static final String CREATED_DATE = "created_date";
    private static final String UPDATED_DATE = "updated_date";

}
