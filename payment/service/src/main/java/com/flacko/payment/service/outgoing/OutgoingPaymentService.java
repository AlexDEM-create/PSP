package com.flacko.payment.service.outgoing;

import com.flacko.common.exception.OutgoingPaymentNotFoundException;

public interface OutgoingPaymentService {

    OutgoingPaymentBuilder create();

    OutgoingPaymentBuilder update(String id) throws OutgoingPaymentNotFoundException;

    OutgoingPaymentListBuilder list();

    OutgoingPayment get(String id) throws OutgoingPaymentNotFoundException;

}
