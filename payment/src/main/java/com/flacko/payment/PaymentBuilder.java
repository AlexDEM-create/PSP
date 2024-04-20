package com.flacko.payment;

import com.flacko.payment.exception.PaymentMissingRequiredAttributeException;

public interface PaymentBuilder {

    PaymentBuilder withMerchantId(String merchantId);

    PaymentBuilder withTraderTeamId(String traderTeamId);

    PaymentBuilder withCardId(String cardId);

    PaymentBuilder withCurrentState(PaymentState currentState);

    Payment build() throws PaymentMissingRequiredAttributeException;

}
