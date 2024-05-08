package com.flacko.payment.method.webapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flacko.common.currency.Currency;
import com.flacko.payment.method.service.PaymentMethodType;

import java.time.ZonedDateTime;

public record PaymentMethodResponse(@JsonProperty(ID) String id,
                                    @JsonProperty(TYPE) PaymentMethodType type,
                                    @JsonProperty(NUMBER) String number,
                                    @JsonProperty(HOLDER_NAME) String holderName,
                                    @JsonProperty(CURRENCY) Currency currency,
                                    @JsonProperty(BANK_ID) String bankId,
                                    @JsonProperty(TRADER_TEAM_ID) String traderTeamId,
                                    @JsonProperty(TERMINAL_ID) String terminalId,
                                    @JsonProperty(BUSY) boolean busy,
                                    @JsonProperty(CREATED_DATE) ZonedDateTime createdDate,
                                    @JsonProperty(UPDATED_DATE) ZonedDateTime updatedDate) {

    private static final String ID = "id";
    private static final String TYPE = "type";
    private static final String NUMBER = "number";
    private static final String HOLDER_NAME = "holder_name";
    private static final String CURRENCY = "currency";
    private static final String BANK_ID = "bank_id";
    private static final String TRADER_TEAM_ID = "trader_team_id";
    private static final String TERMINAL_ID = "terminal_id";
    private static final String BUSY = "busy";
    private static final String CREATED_DATE = "created_date";
    private static final String UPDATED_DATE = "updated_date";

}
