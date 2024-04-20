package com.flacko.payment.verification.receipt;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.Map;

public interface ReceiptPaymentVerification {

    int MAX_RECEIPT_SIZE = 256 * 1024;

    Long getPrimaryKey();

    String getId();

    String getPaymentId();

    String getRecipientFullName();

    String getRecipientCardLastFourDigits();

    String getSenderFullName();

    String getSenderCardLastFourDigits();

    BigDecimal getAmount();

    Currency getAmountCurrency();

    BigDecimal getCommission();

    Currency getCommissionCurrency();

    Map<String, Object> getData();

    Instant getCreatedDate();

}
