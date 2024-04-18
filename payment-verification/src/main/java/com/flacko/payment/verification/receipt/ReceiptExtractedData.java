package com.flacko.payment.verification.receipt;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Currency;

public record ReceiptExtractedData(@JsonProperty(RECIPIENT_FULL_NAME) String recipientFullName,
                                   @JsonProperty(RECIPIENT_CARD_LAST_FOUR_DIGITS) String recipientCardLastFourDigits,
                                   @JsonProperty(SENDER_FULL_NAME) String senderFullName,
                                   @JsonProperty(SENDER_CARD_LAST_FOUR_DIGITS) String senderCardLastFourDigits,
                                   @JsonProperty(AMOUNT) BigDecimal amount,
                                   @JsonProperty(AMOUNT_CURRENCY) Currency amountCurrency,
                                   @JsonProperty(COMMISSION) BigDecimal commission,
                                   @JsonProperty(COMMISSION_CURRENCY) Currency commissionCurrency) {

    private static final String RECIPIENT_FULL_NAME = "recipient_full_name";
    private static final String RECIPIENT_CARD_LAST_FOUR_DIGITS = "recipient_card_last_four_digits";
    private static final String SENDER_FULL_NAME = "sender_full_name";
    private static final String SENDER_CARD_LAST_FOUR_DIGITS = "sender_card_last_four_digits";
    private static final String AMOUNT = "amount";
    private static final String AMOUNT_CURRENCY = "amount_currency";
    private static final String COMMISSION = "commission";
    private static final String COMMISSION_CURRENCY = "commission_currency";

}
