package com.flacko.payment.service.incoming;

import com.flacko.common.state.PaymentState;

import java.util.List;

public interface IncomingPaymentListBuilder {

    IncomingPaymentListBuilder withMerchantId(String merchantId);

    IncomingPaymentListBuilder withTraderTeamId(String traderTeamId);

    IncomingPaymentListBuilder withPaymentMethodId(String paymentMethodId);

    IncomingPaymentListBuilder withCurrentState(PaymentState currentState);

    List<IncomingPayment> build();

}
