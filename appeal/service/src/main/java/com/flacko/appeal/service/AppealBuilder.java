package com.flacko.appeal.service;

import com.flacko.appeal.service.exception.AppealIllegalPaymentCurrentStateException;
import com.flacko.appeal.service.exception.AppealIllegalStateTransitionException;
import com.flacko.appeal.service.exception.AppealMissingRequiredAttributeException;
import com.flacko.common.exception.PaymentNotFoundException;

public interface AppealBuilder {

    AppealBuilder withPaymentId(String paymentId);

    AppealBuilder withSource(AppealSource source);

    AppealBuilder withState(AppealState newState) throws AppealIllegalStateTransitionException;

    Appeal build() throws AppealMissingRequiredAttributeException, PaymentNotFoundException,
            AppealIllegalPaymentCurrentStateException;

}