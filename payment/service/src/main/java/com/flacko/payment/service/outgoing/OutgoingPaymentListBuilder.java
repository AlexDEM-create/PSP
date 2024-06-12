package com.flacko.payment.service.outgoing;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.common.payment.RecipientPaymentMethodType;
import com.flacko.common.state.PaymentState;

import java.time.Instant;
import java.util.List;

public interface OutgoingPaymentListBuilder {

    OutgoingPaymentListBuilder withMerchantId(String merchantId);

    OutgoingPaymentListBuilder withTraderTeamId(String traderTeamId);

    OutgoingPaymentListBuilder withPaymentMethodId(String paymentMethodId);

    OutgoingPaymentListBuilder withCurrency(Currency currency);

    OutgoingPaymentListBuilder withRecipient(String recipient);

    OutgoingPaymentListBuilder withBank(Bank bank);

    OutgoingPaymentListBuilder withRecipientPaymentMethodType(RecipientPaymentMethodType recipientPaymentMethodType);

    OutgoingPaymentListBuilder withCurrentState(PaymentState currentState);

    OutgoingPaymentListBuilder withStartDate(Instant startDate);

    OutgoingPaymentListBuilder withEndDate(Instant endDate);

    List<OutgoingPayment> build();

}
