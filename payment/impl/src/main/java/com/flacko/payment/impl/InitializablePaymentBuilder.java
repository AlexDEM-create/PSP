package com.flacko.payment.impl;

import com.flacko.payment.service.Payment;
import com.flacko.payment.service.PaymentBuilder;

public interface InitializablePaymentBuilder extends PaymentBuilder {

    PaymentBuilder initializeNew();

    PaymentBuilder initializeExisting(Payment existingPayment);

}
