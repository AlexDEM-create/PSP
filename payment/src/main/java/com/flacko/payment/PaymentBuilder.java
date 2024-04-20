package com.flacko.payment;

import com.flacko.payment.exception.PaymentIllegalStateTransitionException;
import com.flacko.payment.exception.PaymentMissingRequiredAttributeException;

public interface PaymentBuilder {

    PaymentBuilder withMerchantId(String merchantId);

    PaymentBuilder withTraderTeamId(String traderTeamId);

    PaymentBuilder withCardId(String cardId);

    PaymentBuilder withState(PaymentState newState) throws PaymentIllegalStateTransitionException;

    Payment build() throws PaymentMissingRequiredAttributeException;

}
