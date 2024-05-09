package com.flacko.payment.verification.sms.service;

import com.flacko.common.currency.Currency;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public interface SmsPaymentVerification {

    Long getPrimaryKey();

    String getId();

    String getIncomingPaymentId();

    String getRecipientCardLastFourDigits();

    String getSenderFullName();

    BigDecimal getAmount();

    Currency getAmountCurrency();

    String getMessage();

    Map<String, Object> getData();

    Instant getCreatedDate();

}
