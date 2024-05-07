package com.flacko.outgoing.payment.impl;

import com.flacko.outgoing.payment.service.OutgoingPayment;
import com.flacko.payment.service.Payment;
import com.flacko.payment.service.OutgoingPaymentBuilder;

public interface InitializableOutgoingPaymentBuilder extends com.flacko.outgoing.payment.service.OutgoingPaymentBuilder {

    OutgoingPaymentBuilder initializeNew();

    OutgoingPaymentBuilder initializeExisting(OutgoingPayment existingPayment);

}
