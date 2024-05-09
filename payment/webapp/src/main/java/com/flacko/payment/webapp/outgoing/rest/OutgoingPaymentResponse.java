package com.flacko.payment.webapp.outgoing.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flacko.common.currency.Currency;
import com.flacko.common.state.PaymentState;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record OutgoingPaymentResponse(@JsonProperty(ID) String id,
                                      @JsonProperty(MERCHANT_ID) String merchantId,
                                      @JsonProperty(TRADER_TEAM_ID) String traderTeamId,
                                      @JsonProperty(PAYMENT_METHOD_ID) String paymentMethodId,
                                      @JsonProperty(AMOUNT) BigDecimal amount,
                                      @JsonProperty(CURRENCY) Currency currency,
                                      @JsonProperty(CURRENT_STATE) PaymentState currentState,
                                      @JsonProperty(CREATED_DATE) ZonedDateTime createdDate,
                                      @JsonProperty(UPDATED_DATE) ZonedDateTime updatedDate) {

    private static final String ID = "id";
    private static final String MERCHANT_ID = "merchant_id";
    private static final String TRADER_TEAM_ID = "trader_team_id";
    private static final String PAYMENT_METHOD_ID = "payment_method_id";
    private static final String AMOUNT = "amount";
    private static final String CURRENCY = "currency";
    private static final String CURRENT_STATE = "current_state";
    private static final String CREATED_DATE = "created_date";
    private static final String UPDATED_DATE = "updated_date";

}
