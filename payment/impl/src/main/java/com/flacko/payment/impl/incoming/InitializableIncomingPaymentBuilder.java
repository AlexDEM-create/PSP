package com.flacko.payment.impl.incoming;

import com.flacko.payment.service.incoming.IncomingPayment;
import com.flacko.payment.service.incoming.IncomingPaymentBuilder;

public interface InitializableIncomingPaymentBuilder extends IncomingPaymentBuilder {

    IncomingPaymentBuilder initializeNew();

    IncomingPaymentBuilder initializeExisting(IncomingPayment existingIncomingPayment);

}
