package com.flacko.payment.method.service;

import com.flacko.common.currency.Currency;

import java.util.List;

public interface PaymentMethodListBuilder {

    PaymentMethodListBuilder withType(PaymentMethodType type);

    PaymentMethodListBuilder withCurrency(Currency currency);

    PaymentMethodListBuilder withBankId(String bankId);

    PaymentMethodListBuilder withTraderTeamId(String traderTeamId);

    PaymentMethodListBuilder withTerminalId(String terminalId);

    PaymentMethodListBuilder withBusy(boolean busy);

    List<PaymentMethod> build();

}
