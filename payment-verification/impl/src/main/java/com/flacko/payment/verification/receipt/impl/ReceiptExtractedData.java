package com.flacko.payment.verification.receipt.impl;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ReceiptExtractedData {

    private static final String RECIPIENT_FULL_NAME = "recipient_full_name";
    private static final String RECIPIENT_CARD_LAST_FOUR_DIGITS = "recipient_card_last_four_digits";
    private static final String SENDER_FULL_NAME = "sender_full_name";
    private static final String SENDER_CARD_LAST_FOUR_DIGITS = "sender_card_last_four_digits";
    private static final String AMOUNT = "amount";
    private static final String AMOUNT_CURRENCY = "amount_currency";
    private static final String COMMISSION = "commission";
    private static final String COMMISSION_CURRENCY = "commission_currency";
    private static final String AUTHORIZATION_CODE = "authorization_code";
    private static final String DOCUMENT_NUMBER = "document_number";

    private final String recipientFullName;
    private final String recipientCardLastFourDigits;
    private final String senderFullName;
    private final String senderCardLastFourDigits;
    private final String amount;
    private final String amountCurrency;
    private final String commission;
    private final String commissionCurrency;
    private Map<String, Object> data = new HashMap<>();

    public ReceiptExtractedData(
            @JsonProperty(RECIPIENT_FULL_NAME) String recipientFullName,
            @JsonProperty(RECIPIENT_CARD_LAST_FOUR_DIGITS) String recipientCardLastFourDigits,
            @JsonProperty(SENDER_FULL_NAME) String senderFullName,
            @JsonProperty(SENDER_CARD_LAST_FOUR_DIGITS) String senderCardLastFourDigits,
            @JsonProperty(AMOUNT) String amount,
            @JsonProperty(AMOUNT_CURRENCY) String amountCurrency,
            @JsonProperty(COMMISSION) String commission,
            @JsonProperty(COMMISSION_CURRENCY) String commissionCurrency) {
        this.recipientFullName = recipientFullName;
        this.recipientCardLastFourDigits = recipientCardLastFourDigits;
        this.senderFullName = senderFullName;
        this.senderCardLastFourDigits = senderCardLastFourDigits;
        this.amount = amount;
        this.amountCurrency = amountCurrency;
        this.commission = commission;
        this.commissionCurrency = commissionCurrency;
    }

    @JsonAnyGetter
    public Map<String, Object> getData() {
        return data;
    }

    @JsonAnySetter
    public void setData(String key, Object value) {
        data.put(key, value);
    }

}
