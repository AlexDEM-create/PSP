package com.flacko.payment;

import com.flacko.card.exception.CardNotFoundException;
import com.flacko.merchant.exception.MerchantNotFoundException;
import com.flacko.payment.exception.PaymentIllegalStateTransitionException;
import com.flacko.payment.exception.PaymentInvalidAmountException;
import com.flacko.payment.exception.PaymentMissingRequiredAttributeException;
import com.flacko.trader.team.exception.TraderTeamNotFoundException;

public interface PaymentBuilder {

    PaymentBuilder withMerchantId(String merchantId);

    PaymentBuilder withTraderTeamId(String traderTeamId);

    PaymentBuilder withCardId(String cardId);

    PaymentBuilder withState(PaymentState newState) throws PaymentIllegalStateTransitionException;

    Payment build() throws PaymentMissingRequiredAttributeException, TraderTeamNotFoundException,
            MerchantNotFoundException, CardNotFoundException, PaymentInvalidAmountException;

}
