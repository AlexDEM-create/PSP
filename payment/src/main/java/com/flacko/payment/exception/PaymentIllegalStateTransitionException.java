package com.flacko.payment.exception;

import com.flacko.payment.PaymentState;

public class PaymentIllegalStateTransitionException extends Exception {

    public PaymentIllegalStateTransitionException(String id, PaymentState currentState, PaymentState newState) {
        super(String.format("Payment %s state cannot be changed from %s to %s", id, currentState, newState));
    }

}
