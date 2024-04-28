package com.flacko.payment.service;

import com.flacko.common.exception.CardNotFoundException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.state.PaymentState;
import com.flacko.payment.service.exception.PaymentIllegalStateTransitionException;
import com.flacko.payment.service.exception.PaymentInvalidAmountException;
import com.flacko.payment.service.exception.PaymentMissingRequiredAttributeException;

public interface PaymentBuilder {

    PaymentBuilder withMerchantId(String merchantId);

    PaymentBuilder withTraderTeamId(String traderTeamId);

    PaymentBuilder withCardId(String cardId);

    PaymentBuilder withState(PaymentState newState) throws PaymentIllegalStateTransitionException;

    Payment build() throws PaymentMissingRequiredAttributeException, TraderTeamNotFoundException,
            MerchantNotFoundException, CardNotFoundException, PaymentInvalidAmountException;

}
