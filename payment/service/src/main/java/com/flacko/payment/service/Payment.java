package com.flacko.payment.service;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.common.payment.RecipientPaymentMethodType;
import com.flacko.common.state.PaymentState;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

public interface Payment {

    Long getPrimaryKey();

    String getId();

    String getMerchantId();

    String getTraderTeamId();

    BigDecimal getAmount();

    Currency getCurrency();

    Bank getBank();

    PaymentState getCurrentState();

    Instant getCreatedDate();

    Instant getUpdatedDate();

}
