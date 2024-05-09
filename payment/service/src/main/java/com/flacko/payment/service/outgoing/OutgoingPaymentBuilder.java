package com.flacko.payment.service.outgoing;

import com.flacko.common.currency.Currency;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.PaymentMethodNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.state.PaymentState;
import com.flacko.payment.service.outgoing.exception.OutgoingPaymentIllegalStateTransitionException;
import com.flacko.payment.service.outgoing.exception.OutgoingPaymentInvalidAmountException;
import com.flacko.payment.service.outgoing.exception.OutgoingPaymentMissingRequiredAttributeException;

import java.math.BigDecimal;

public interface OutgoingPaymentBuilder {

    OutgoingPaymentBuilder withMerchantId(String merchantId);

    OutgoingPaymentBuilder withTraderTeamId(String traderTeamId);

    OutgoingPaymentBuilder withPaymentMethodId(String paymentMethodId);

    OutgoingPaymentBuilder withAmount(BigDecimal amount);

    OutgoingPaymentBuilder withCurrency(Currency currency);

    OutgoingPaymentBuilder withState(PaymentState newState) throws OutgoingPaymentIllegalStateTransitionException;

    OutgoingPayment build() throws OutgoingPaymentMissingRequiredAttributeException, TraderTeamNotFoundException,
            MerchantNotFoundException, PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException;

}
