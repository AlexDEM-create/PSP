package com.flacko.card.service;

import com.flacko.common.currency.Currency;

import java.time.Instant;
import java.util.Optional;

public interface Card {

    Long getPrimaryKey();

    String getId();

    PaymentMethodType getType();

    String getNumber();

    String getBankId();

    String getTraderTeamId();

    String getTerminalId();

    Currency getCurrency();

    String getCardHolder();

    boolean isBusy();

    Instant getCreatedDate();

    Instant getUpdatedDate();

    Optional<Instant> getDeletedDate();

}
