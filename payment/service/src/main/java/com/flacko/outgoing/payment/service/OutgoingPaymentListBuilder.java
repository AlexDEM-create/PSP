package com.flacko.outgoing.payment.service;

import com.flacko.common.state.PaymentState;

import java.util.List;

public interface OutgoingPaymentListBuilder {

    OutgoingPaymentListBuilder withMerchantId(String merchantId);

    OutgoingPaymentListBuilder withTraderTeamId(String traderTeamId);

    OutgoingPaymentListBuilder withCardId(String cardId);

    OutgoingPaymentListBuilder withDirection(PaymentDirection direction);

    OutgoingPaymentListBuilder withCurrentState(PaymentState currentState);

    List<OutgoingPayment> build();

}
