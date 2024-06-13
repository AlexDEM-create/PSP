package com.flacko.payment.service.outgoing;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.common.exception.MerchantInsufficientOutgoingBalanceException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.NoEligibleTraderTeamsException;
import com.flacko.common.exception.OutgoingPaymentIllegalStateTransitionException;
import com.flacko.common.exception.OutgoingPaymentInvalidAmountException;
import com.flacko.common.exception.OutgoingPaymentMissingRequiredAttributeException;
import com.flacko.common.exception.PaymentMethodNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.payment.RecipientPaymentMethodType;
import com.flacko.common.state.PaymentState;

import java.math.BigDecimal;
import java.util.Optional;

public interface OutgoingPaymentBuilder {

    OutgoingPaymentBuilder withRandomTraderTeamId(Optional<String> currentTraderTeamId)
            throws NoEligibleTraderTeamsException, TraderTeamNotFoundException;

    OutgoingPaymentBuilder withPaymentMethodId(String paymentMethodId);

    OutgoingPaymentBuilder withAmount(BigDecimal amount);

    OutgoingPaymentBuilder withCurrency(Currency currency);

    OutgoingPaymentBuilder withRecipient(String recipient);

    OutgoingPaymentBuilder withBank(Bank bank);

    OutgoingPaymentBuilder withRecipientPaymentMethodType(RecipientPaymentMethodType recipientPaymentMethodType);

    OutgoingPaymentBuilder withPartnerPaymentId(String partnerPaymentId);

    OutgoingPaymentBuilder withState(PaymentState newState) throws OutgoingPaymentIllegalStateTransitionException;

    OutgoingPayment build() throws OutgoingPaymentMissingRequiredAttributeException, TraderTeamNotFoundException,
            MerchantNotFoundException, PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException,
            MerchantInsufficientOutgoingBalanceException;

}
