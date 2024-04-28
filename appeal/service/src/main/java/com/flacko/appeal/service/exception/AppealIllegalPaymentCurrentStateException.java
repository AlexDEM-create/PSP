package com.flacko.appeal.service.exception;

import com.flacko.common.state.PaymentState;

public class AppealIllegalPaymentCurrentStateException extends Exception {

    public AppealIllegalPaymentCurrentStateException(String id, String paymentId, PaymentState currentState) {
        super(String.format("Illegal current state %s for payment %s to start an appeal %s", currentState, paymentId,
                id));
    }

}
