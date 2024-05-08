package com.flacko.incoming.payment.impl;

import com.flacko.payment.service.Payment;
import com.flacko.payment.service.OutgoingPaymentBuilder;

public interface InitializableIncomingPaymentBuilder extends OutgoingPaymentBuilder {

    OutgoingPaymentBuilder initializeNew();

    OutgoingPaymentBuilder initializeExisting(Payment existingPayment);

}
