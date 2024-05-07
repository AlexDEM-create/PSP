package com.flacko.outgoing.payment.service;

import com.flacko.common.currency.Currency;
import com.flacko.common.exception.CardNotFoundException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.state.PaymentState;
import com.flacko.outgoing.payment.service.exception.OutgoingPaymentIllegalStateTransitionException;
import com.flacko.outgoing.payment.service.exception.OutgoingPaymentInvalidAmountException;
import com.flacko.outgoing.payment.service.exception.OutgoingPaymentMissingRequiredAttributeException;

import java.math.BigDecimal;

public interface OutgoingPaymentBuilder {

    OutgoingPaymentBuilder withMerchantId(String merchantId);

    OutgoingPaymentBuilder withTraderTeamId(String traderTeamId);

    OutgoingPaymentBuilder withCardId(String cardId);

    OutgoingPaymentBuilder withAmount(BigDecimal amount);

    OutgoingPaymentBuilder withCurrency(Currency currency);

    OutgoingPaymentBuilder withDirection(PaymentDirection direction);

    OutgoingPaymentBuilder withState(PaymentState newState) throws OutgoingPaymentIllegalStateTransitionException;

    OutgoingPayment build() throws OutgoingPaymentMissingRequiredAttributeException, TraderTeamNotFoundException,
            MerchantNotFoundException, CardNotFoundException, OutgoingPaymentInvalidAmountException;

}
