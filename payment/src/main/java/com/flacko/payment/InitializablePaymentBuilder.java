package com.flacko.payment;

public interface InitializablePaymentBuilder extends PaymentBuilder {

    PaymentBuilder initializeNew();

    PaymentBuilder initializeExisting(Payment existingPayment);

}
