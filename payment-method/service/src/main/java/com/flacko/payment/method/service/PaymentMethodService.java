package com.flacko.payment.method.service;

import com.flacko.common.exception.PaymentMethodNotFoundException;

public interface PaymentMethodService {

    PaymentMethodBuilder create();

    PaymentMethodBuilder update(String id) throws PaymentMethodNotFoundException;

    PaymentMethodListBuilder list();

    PaymentMethod get(String id) throws PaymentMethodNotFoundException;

}
