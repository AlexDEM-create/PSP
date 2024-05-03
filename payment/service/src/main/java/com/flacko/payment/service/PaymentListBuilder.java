package com.flacko.payment.service;

import com.flacko.common.state.PaymentState;

import java.util.List;

public interface PaymentListBuilder {

    PaymentListBuilder withMerchantId(String merchantId);

    PaymentListBuilder withTraderTeamId(String traderTeamId);

    PaymentListBuilder withCardId(String cardId);

    PaymentListBuilder withDirection(PaymentDirection direction);

    PaymentListBuilder withCurrentState(PaymentState currentState);

    List<Payment> build();

}
