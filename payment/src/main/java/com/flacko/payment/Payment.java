package com.flacko.payment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

public interface Payment {

    Long getPrimaryKey();

    String getId();

    String getMerchantId();

    String getTraderId();

    String getCardId();

    BigDecimal getAmount();

    Currency getCurrency();

    PaymentDirection getDirection();

    PaymentState getCurrentState();

    Instant getCreatedDate();

    Instant getUpdatedDate();

}
