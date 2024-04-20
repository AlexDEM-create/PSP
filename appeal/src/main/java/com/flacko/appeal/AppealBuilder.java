package com.flacko.appeal;

import com.flacko.appeal.exception.AppealIllegalStateTransitionException;
import com.flacko.appeal.exception.AppealMissingRequiredAttributeException;

public interface AppealBuilder {

    AppealBuilder withPaymentId(String paymentId);

    AppealBuilder withState(AppealState newState) throws AppealIllegalStateTransitionException;

    Appeal build() throws AppealMissingRequiredAttributeException;

}