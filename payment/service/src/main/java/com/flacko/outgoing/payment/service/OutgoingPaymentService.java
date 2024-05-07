package com.flacko.outgoing.payment.service;

import com.flacko.common.exception.PaymentNotFoundException;

public interface OutgoingPaymentService {

    OutgoingPaymentBuilder create();

    OutgoingPaymentBuilder update(String id) throws PaymentNotFoundException;

    OutgoingPaymentListBuilder list();

    OutgoingPayment get(String id) throws PaymentNotFoundException;

}
