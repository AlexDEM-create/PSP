package com.flacko.payment.method.webapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;

import java.util.Optional;

public record PaymentMethodCreateRequest(@JsonProperty(NUMBER) String number,
                                         @JsonProperty(ACCOUNT_LAST_FOUR_DIGITS) String accountLastFourDigits,
                                         @JsonProperty(FIRST_NAME) String firstName,
                                         @JsonProperty(LAST_NAME) String lastName,
                                         @JsonProperty(CURRENCY) Currency currency,
                                         @JsonProperty(BANK) Bank bank,
                                         @JsonProperty(TRADER_TEAM_ID) String traderTeamId,
                                         @JsonProperty(TERMINAL_ID) Optional<String> terminalId) {

    private static final String NUMBER = "number";
    private static final String ACCOUNT_LAST_FOUR_DIGITS = "account_last_four_digits";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String CURRENCY = "currency";
    private static final String BANK = "bank";
    private static final String TRADER_TEAM_ID = "trader_team_id";
    private static final String TERMINAL_ID = "terminal_id";

}
