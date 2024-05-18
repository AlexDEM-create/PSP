package com.flacko.payment.verification.webapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.Map;

public record ReceiptPaymentVerificationResponse(@JsonProperty(ID) String id,
                                                 @JsonProperty(OUTGOING_PAYMENT_ID) String outgoingPaymentId,
                                                 @JsonProperty(DATA) Map<String, Object> data,
                                                 @JsonProperty(CREATED_DATE) ZonedDateTime createdDate) {

    private static final String ID = "id";
    private static final String OUTGOING_PAYMENT_ID = "outgoing_payment_id";
    private static final String DATA = "data";
    private static final String CREATED_DATE = "created_date";

}
