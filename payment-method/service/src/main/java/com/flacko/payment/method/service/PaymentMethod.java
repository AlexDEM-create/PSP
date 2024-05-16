package com.flacko.payment.method.service;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;

import java.time.Instant;
import java.util.Optional;

public interface PaymentMethod {

    Long getPrimaryKey();

    String getId();

    String getNumber();

    String getFirstName();

    String getLastName();

    Currency getCurrency();

    Bank getBank();

    String getTraderTeamId();

    String getTerminalId();

    boolean isBusy();

    Instant getCreatedDate();

    Instant getUpdatedDate();

    Optional<Instant> getDeletedDate();

}
