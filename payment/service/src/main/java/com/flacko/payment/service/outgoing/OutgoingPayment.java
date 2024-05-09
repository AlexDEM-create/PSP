package com.flacko.payment.service.outgoing;

import com.flacko.common.currency.Currency;
import com.flacko.common.state.PaymentState;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

public interface OutgoingPayment {

    Long getPrimaryKey();

    String getId();

    String getMerchantId();

    String getTraderTeamId();

    String getPaymentMethodId();

    BigDecimal getAmount();

    Currency getCurrency();

    PaymentState getCurrentState();

    boolean isBooked();

    Instant getCreatedDate();

    Instant getUpdatedDate();

    Optional<Instant> getBookedDate();

}
