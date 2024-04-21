package com.flacko.appeal;

import com.flacko.appeal.exception.AppealIllegalPaymentCurrentStateException;
import com.flacko.appeal.exception.AppealIllegalStateTransitionException;
import com.flacko.appeal.exception.AppealMissingRequiredAttributeException;
import com.flacko.payment.exception.PaymentNotFoundException;

public interface AppealBuilder {

    AppealBuilder withPaymentId(String paymentId);

    AppealBuilder withState(AppealState newState) throws AppealIllegalStateTransitionException;

    Appeal build() throws AppealMissingRequiredAttributeException, PaymentNotFoundException, AppealIllegalPaymentCurrentStateException;

}