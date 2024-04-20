package com.flacko.appeal;

import com.flacko.appeal.exception.AppealMissingRequiredAttributeException;

public interface AppealBuilder {

    AppealBuilder withPaymentId(String paymentId);

    Appeal build() throws AppealMissingRequiredAttributeException;

}