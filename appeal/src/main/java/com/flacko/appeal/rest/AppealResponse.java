
package com.flacko.appeal.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AppealResponse(@JsonProperty(ID) String id,
                             @JsonProperty(APPEAL_STATUS) Enum appealStatus,
                             @JsonProperty(PAYMENT_ID) String paymentId) {

    private static final String ID = "id";
    private static final String APPEAL_STATUS = "appeal_status";
    private static final String PAYMENT_ID = "payment_id";

}