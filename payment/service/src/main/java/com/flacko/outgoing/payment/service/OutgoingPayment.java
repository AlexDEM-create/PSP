package com.flacko.outgoing.payment.service;

import com.flacko.common.currency.Currency;
import com.flacko.common.state.PaymentState;

import java.math.BigDecimal;
import java.time.Instant;

public interface OutgoingPayment {

    Long getPrimaryKey();

    String getId();

    String getMerchantId();

    String getTraderTeamId();

    String getCardId();

    BigDecimal getAmount();

    Currency getCurrency();

    PaymentDirection getDirection();

    PaymentState getCurrentState();

    Instant getCreatedDate();

    Instant getUpdatedDate();

}
