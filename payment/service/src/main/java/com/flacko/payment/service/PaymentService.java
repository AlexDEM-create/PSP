package com.flacko.payment.service;

import com.flacko.common.exception.PaymentNotFoundException;

public interface PaymentService {

    PaymentBuilder create();

    PaymentBuilder update(String id) throws PaymentNotFoundException;

    PaymentListBuilder list();

    Payment get(String id) throws PaymentNotFoundException;

}
