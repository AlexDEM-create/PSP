package com.flacko.incoming.payment.service;

import com.flacko.common.exception.PaymentNotFoundException;

public interface IncomingPaymentService {

    IncomingPaymentBuilder create();

    IncomingPaymentBuilder update(String id) throws PaymentNotFoundException;

    IncomingPaymentListBuilder list();

    IncomingPayment get(String id) throws PaymentNotFoundException;

}
