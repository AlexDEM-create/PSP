package com.flacko.payment.service.exception;

import com.flacko.common.state.PaymentState;

public class PaymentIllegalStateTransitionException extends Exception {

    public PaymentIllegalStateTransitionException(String id, PaymentState currentState, PaymentState newState) {
        super(String.format("Payment %s state cannot be changed from %s to %s", id, currentState, newState));
    }

}
