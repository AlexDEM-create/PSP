package com.flacko.incoming.payment.service.exception;

import com.flacko.common.state.PaymentState;

public class IncomingPaymentIllegalStateTransitionException extends Exception {

    public IncomingPaymentIllegalStateTransitionException(String id, PaymentState currentState, PaymentState newState) {
        super(String.format("OutgoingPayment %s state cannot be changed from %s to %s", id, currentState, newState));
    }

}
