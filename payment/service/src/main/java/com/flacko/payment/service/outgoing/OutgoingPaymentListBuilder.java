package com.flacko.payment.service.outgoing;

import com.flacko.common.state.PaymentState;

import java.util.List;

public interface OutgoingPaymentListBuilder {

    OutgoingPaymentListBuilder withMerchantId(String merchantId);

    OutgoingPaymentListBuilder withTraderTeamId(String traderTeamId);

    OutgoingPaymentListBuilder withPaymentMethodId(String paymentMethodId);

    OutgoingPaymentListBuilder withCurrentState(PaymentState currentState);

    OutgoingPaymentListBuilder withBooked(Boolean booked);

    List<OutgoingPayment> build();

}
