package com.flacko.payment.service.incoming.exception;

import com.flacko.common.state.PaymentState;

public class IncomingPaymentIllegalStateTransitionException extends Exception {

    public IncomingPaymentIllegalStateTransitionException(String id, PaymentState currentState, PaymentState newState) {
        super(String.format("Incoming payment %s state cannot be changed from %s to %s", id, currentState, newState));
    }

}
