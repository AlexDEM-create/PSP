package com.flacko.payment.method.service;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;

import java.util.List;

public interface PaymentMethodListBuilder {

    PaymentMethodListBuilder withCurrency(Currency currency);

    PaymentMethodListBuilder withBank(Bank bank);

    PaymentMethodListBuilder withTraderTeamId(String traderTeamId);

    PaymentMethodListBuilder withTerminalId(String terminalId);

    PaymentMethodListBuilder withBusy(boolean busy);

    List<PaymentMethod> build();

}
