package com.flacko.payment.impl.outgoing;

import com.flacko.payment.service.outgoing.OutgoingPayment;
import com.flacko.payment.service.outgoing.OutgoingPaymentBuilder;

public interface InitializableOutgoingPaymentBuilder extends OutgoingPaymentBuilder {

    OutgoingPaymentBuilder initializeNew();

    OutgoingPaymentBuilder initializeExisting(OutgoingPayment existingOutgoingPayment);

}
