package com.flacko.payment.impl.outgoing;

import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.UserNotFoundException;
import com.flacko.payment.service.outgoing.OutgoingPayment;
import com.flacko.payment.service.outgoing.OutgoingPaymentBuilder;

public interface InitializableOutgoingPaymentBuilder extends OutgoingPaymentBuilder {

    OutgoingPaymentBuilder initializeNew(String login) throws UserNotFoundException, MerchantNotFoundException;

    OutgoingPaymentBuilder initializeExisting(OutgoingPayment existingOutgoingPayment);

}
