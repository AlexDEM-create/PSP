package com.flacko.payment;

import com.flacko.payment.exception.PaymentMissingRequiredAttributeException;

public interface PaymentBuilder {

    PaymentBuilder withMerchantId(String merchantId);

    PaymentBuilder withTraderId(String traderId);

    PaymentBuilder withCardId(String cardId);

    PaymentBuilder withCurrentState(PaymentState currentState);

    Payment build() throws PaymentMissingRequiredAttributeException;

}
