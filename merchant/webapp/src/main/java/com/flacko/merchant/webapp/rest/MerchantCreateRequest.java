package com.flacko.merchant.webapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flacko.common.country.Country;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;

public record MerchantCreateRequest(@JsonProperty(NAME) String name,
                                    @JsonProperty(USER_ID) String userId,
                                    @JsonProperty(COUNTRY) Country country,
                                    @JsonProperty(INCOMING_FEE_RATE) BigDecimal incomingFeeRate,
                                    @JsonProperty(OUTGOING_FEE_RATE) BigDecimal outgoingFeeRate,
                                    @JsonProperty(WEBHOOK) Optional<URL> webhook) {

    private static final String NAME = "name";
    private static final String USER_ID = "user_id";
    private static final String COUNTRY = "country";
    private static final String INCOMING_FEE_RATE = "incoming_fee_rate";
    private static final String OUTGOING_FEE_RATE = "outgoing_fee_rate";
    private static final String WEBHOOK = "webhook";

}
