package com.flacko.incoming.payment.service;

import com.flacko.common.currency.Currency;
import com.flacko.common.exception.CardNotFoundException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.state.PaymentState;
import com.flacko.incoming.payment.service.exception.IncomingPaymentIllegalStateTransitionException;
import com.flacko.incoming.payment.service.exception.IncomingPaymentInvalidAmountException;
import com.flacko.incoming.payment.service.exception.IncomingPaymentMissingRequiredAttributeException;

import java.math.BigDecimal;

public interface IncomingPaymentBuilder {

    IncomingPaymentBuilder withMerchantId(String merchantId);

    IncomingPaymentBuilder withTraderTeamId(String traderTeamId);

    IncomingPaymentBuilder withCardId(String cardId);

    IncomingPaymentBuilder withAmount(BigDecimal amount);

    IncomingPaymentBuilder withCurrency(Currency currency);

    IncomingPaymentBuilder withDirection(PaymentDirection direction);

    IncomingPaymentBuilder withState(PaymentState newState) throws IncomingPaymentIllegalStateTransitionException;

    IncomingPayment build() throws IncomingPaymentMissingRequiredAttributeException, TraderTeamNotFoundException,
            MerchantNotFoundException, CardNotFoundException, IncomingPaymentInvalidAmountException;

}
