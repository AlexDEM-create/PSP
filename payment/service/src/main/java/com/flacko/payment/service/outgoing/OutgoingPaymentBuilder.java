package com.flacko.payment.service.outgoing;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.PaymentMethodNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.payment.RecipientPaymentMethodType;
import com.flacko.common.state.PaymentState;
import com.flacko.common.exception.OutgoingPaymentIllegalStateTransitionException;
import com.flacko.common.exception.OutgoingPaymentInvalidAmountException;
import com.flacko.common.exception.OutgoingPaymentMissingRequiredAttributeException;

import java.math.BigDecimal;

public interface OutgoingPaymentBuilder {

    OutgoingPaymentBuilder withMerchantId(String merchantId);

    OutgoingPaymentBuilder withTraderTeamId(String traderTeamId);

    OutgoingPaymentBuilder withPaymentMethodId(String paymentMethodId);

    OutgoingPaymentBuilder withAmount(BigDecimal amount);

    OutgoingPaymentBuilder withCurrency(Currency currency);

    OutgoingPaymentBuilder withRecipient(String recipient);

    OutgoingPaymentBuilder withBank(Bank bank);

    OutgoingPaymentBuilder withRecipientPaymentMethodType(RecipientPaymentMethodType recipientPaymentMethodType);

    OutgoingPaymentBuilder withPartnerPaymentId(String partnerPaymentId);

    OutgoingPaymentBuilder withState(PaymentState newState) throws OutgoingPaymentIllegalStateTransitionException;

    OutgoingPayment build() throws OutgoingPaymentMissingRequiredAttributeException, TraderTeamNotFoundException,
            MerchantNotFoundException, PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException;

}
