package com.flacko.payment.method.service;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.common.exception.BankNotFoundException;
import com.flacko.common.exception.TerminalNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.payment.method.service.exception.PaymentMethodInvalidBankCardNumberException;
import com.flacko.payment.method.service.exception.PaymentMethodMissingRequiredAttributeException;

public interface PaymentMethodBuilder {

    PaymentMethodBuilder withNumber(String number);

    PaymentMethodBuilder withFirstName(String firstName);

    PaymentMethodBuilder withLastName(String lastName);

    PaymentMethodBuilder withCurrency(Currency currency);

    PaymentMethodBuilder withBank(Bank bank);

    PaymentMethodBuilder withTraderTeamId(String traderTeamId);

    PaymentMethodBuilder withTerminalId(String terminalId);

    PaymentMethodBuilder withBusy(boolean busy);

    PaymentMethodBuilder withArchived();

    PaymentMethod build() throws PaymentMethodMissingRequiredAttributeException, TraderTeamNotFoundException,
            PaymentMethodInvalidBankCardNumberException, BankNotFoundException, TerminalNotFoundException;

}
