package com.flacko.payment.service.incoming;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.common.state.PaymentState;
import com.flacko.payment.service.outgoing.OutgoingPaymentListBuilder;

import java.time.Instant;
import java.util.List;

public interface IncomingPaymentListBuilder {

    IncomingPaymentListBuilder withMerchantId(String merchantId);

    IncomingPaymentListBuilder withTraderTeamId(String traderTeamId);

    IncomingPaymentListBuilder withPaymentMethodId(String paymentMethodId);

    IncomingPaymentListBuilder withCurrency(Currency currency);

    IncomingPaymentListBuilder withBank(Bank bank);

    IncomingPaymentListBuilder withCurrentState(PaymentState currentState);

    IncomingPaymentListBuilder withStartDate(Instant startDate);

    IncomingPaymentListBuilder withEndDate(Instant endDate);

    List<IncomingPayment> build();

}
