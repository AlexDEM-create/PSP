package com.flacko.incoming.payment.service;

import com.flacko.common.state.PaymentState;

import java.util.List;

public interface IncomingPaymentListBuilder {

    IncomingPaymentListBuilder withMerchantId(String merchantId);

    IncomingPaymentListBuilder withTraderTeamId(String traderTeamId);

    IncomingPaymentListBuilder withCardId(String cardId);

    IncomingPaymentListBuilder withDirection(PaymentDirection direction);

    IncomingPaymentListBuilder withCurrentState(PaymentState currentState);

    List<IncomingPayment> build();

}
