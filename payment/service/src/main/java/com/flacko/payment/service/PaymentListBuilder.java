package com.flacko.payment.service;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.common.state.PaymentState;

import java.time.Instant;
import java.util.List;

public interface PaymentListBuilder {

    PaymentListBuilder withMerchantId(String merchantId);

    PaymentListBuilder withTraderTeamId(String traderTeamId);

    PaymentListBuilder withPaymentMethodId(String paymentMethodId);

    PaymentListBuilder withCurrency(Currency currency);

    PaymentListBuilder withBank(Bank bank);

    PaymentListBuilder withCurrentState(PaymentState currentState);

    PaymentListBuilder withStartDate(Instant startDate);

    PaymentListBuilder withEndDate(Instant endDate);

    List<Payment> build();

}
