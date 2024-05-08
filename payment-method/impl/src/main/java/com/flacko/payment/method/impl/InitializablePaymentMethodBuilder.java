package com.flacko.payment.method.impl;

import com.flacko.payment.method.service.PaymentMethod;
import com.flacko.payment.method.service.PaymentMethodBuilder;

public interface InitializablePaymentMethodBuilder extends PaymentMethodBuilder {

    PaymentMethodBuilder initializeNew();

    PaymentMethodBuilder initializeExisting(PaymentMethod existingPaymentMethod);

}
