package com.flacko.outgoing.payment.service.exception;

import com.flacko.common.state.PaymentState;

public class OutgoingPaymentIllegalStateTransitionException extends Exception {

    public OutgoingPaymentIllegalStateTransitionException(String id, PaymentState currentState, PaymentState newState) {
        super(String.format("OutgoingPayment %s state cannot be changed from %s to %s", id, currentState, newState));
    }

}
