package com.flacko.common.exception;

import com.flacko.common.state.PaymentState;

public class OutgoingPaymentIllegalStateTransitionException extends Exception {

    public OutgoingPaymentIllegalStateTransitionException(String id, PaymentState currentState, PaymentState newState) {
        super(String.format("Outgoing payment %s state cannot be changed from %s to %s", id, currentState, newState));
    }

}
