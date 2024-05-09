package com.flacko.payment.service.incoming;

import com.flacko.common.exception.IncomingPaymentNotFoundException;

public interface IncomingPaymentService {

    IncomingPaymentBuilder create();

    IncomingPaymentBuilder update(String id) throws IncomingPaymentNotFoundException;

    IncomingPaymentListBuilder list();

    IncomingPayment get(String id) throws IncomingPaymentNotFoundException;

}
