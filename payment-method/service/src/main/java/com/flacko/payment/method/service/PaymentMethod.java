package com.flacko.payment.method.service;

import com.flacko.common.currency.Currency;

import java.time.Instant;
import java.util.Optional;

public interface PaymentMethod {

    Long getPrimaryKey();

    String getId();

    PaymentMethodType getType();

    String getNumber();

    String getHolderName();

    Currency getCurrency();

    String getBankId();

    String getTraderTeamId();

    String getTerminalId();

    boolean isBusy();

    Instant getCreatedDate();

    Instant getUpdatedDate();

    Optional<Instant> getDeletedDate();

}
