package com.flacko.payment.service.outgoing;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.common.exception.*;
import com.flacko.common.payment.RecipientPaymentMethodType;
import com.flacko.common.state.PaymentState;

import java.math.BigDecimal;

public interface OutgoingPaymentBuilder {

    OutgoingPaymentBuilder withRandomTraderTeamId() throws NoEligibleTraderTeamsException;

    OutgoingPaymentBuilder withPaymentMethodId(String paymentMethodId);

    OutgoingPaymentBuilder withAmount(BigDecimal amount);

    OutgoingPaymentBuilder withCurrency(Currency currency);

    OutgoingPaymentBuilder withRecipient(String recipient);

    OutgoingPaymentBuilder withBank(Bank bank);

    OutgoingPaymentBuilder withRecipientPaymentMethodType(RecipientPaymentMethodType recipientPaymentMethodType);

    OutgoingPaymentBuilder withPartnerPaymentId(String partnerPaymentId);

    OutgoingPaymentBuilder withState(PaymentState newState) throws OutgoingPaymentIllegalStateTransitionException;

    OutgoingPayment build() throws OutgoingPaymentMissingRequiredAttributeException, TraderTeamNotFoundException,
            MerchantNotFoundException, PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException, MerchantInsufficientOutgoingBalanceException;

}
