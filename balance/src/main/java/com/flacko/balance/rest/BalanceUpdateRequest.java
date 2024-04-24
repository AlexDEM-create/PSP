package com.flacko.balance.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record BalanceUpdateRequest(@JsonProperty(AMOUNT) BigDecimal amount) {

    private static final String AMOUNT = "amount";

}
