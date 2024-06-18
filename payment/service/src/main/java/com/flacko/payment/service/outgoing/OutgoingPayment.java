package com.flacko.payment.service.outgoing;

import com.flacko.common.payment.RecipientPaymentMethodType;
import com.flacko.payment.service.Payment;

import java.util.Optional;

public interface OutgoingPayment extends Payment {

    Optional<String> getPaymentMethodId();

    String getRecipient();

    RecipientPaymentMethodType getRecipientPaymentMethodType();

    Optional<String> getPartnerPaymentId();

}
