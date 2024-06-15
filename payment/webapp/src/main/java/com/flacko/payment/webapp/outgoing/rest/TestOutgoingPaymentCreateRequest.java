package com.flacko.payment.webapp.outgoing.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.common.payment.RecipientPaymentMethodType;

import java.math.BigDecimal;
import java.util.Optional;

public record TestOutgoingPaymentCreateRequest(@JsonProperty(AMOUNT) BigDecimal amount,
                                               @JsonProperty(CURRENCY) Currency currency,
                                               @JsonProperty(RECIPIENT) String recipient,
                                               @JsonProperty(BANK) Bank bank,
                                               @JsonProperty(PAYMENT_METHOD)
                                               RecipientPaymentMethodType recipientPaymentMethodType,
                                               @JsonProperty(TRADER_TEAM_ID) String traderTeamId,
                                               @JsonProperty(MERCHANT_ID) String merchantId,
                                               @JsonProperty(PARTNER_PAYMENT_ID) Optional<String> partnerPaymentId) {

    private static final String AMOUNT = "amount";
    private static final String CURRENCY = "currency";
    private static final String RECIPIENT = "recipient";
    private static final String BANK = "bank";
    private static final String PAYMENT_METHOD = "payment_method";
    private static final String TRADER_TEAM_ID = "trader_team_id";
    private static final String MERCHANT_ID = "merchant_id";
    private static final String PARTNER_PAYMENT_ID = "partner_payment_id";

}
