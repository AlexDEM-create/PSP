package com.flacko.payment.service.outgoing;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.common.payment.RecipientPaymentMethodType;
import com.flacko.common.state.PaymentState;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

public interface OutgoingPayment {

    Long getPrimaryKey();

    String getId();

    String getMerchantId();

    String getTraderTeamId();

    Optional<String> getPaymentMethodId();

    BigDecimal getAmount();

    Currency getCurrency();

    String getRecipient();

    Bank getBank();

    RecipientPaymentMethodType getRecipientPaymentMethodType();

    String getPartnerPaymentId();

    PaymentState getCurrentState();

    Instant getCreatedDate();

    Instant getUpdatedDate();

}
