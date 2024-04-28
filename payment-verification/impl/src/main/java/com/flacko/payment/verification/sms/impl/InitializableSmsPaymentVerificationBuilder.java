package com.flacko.payment.verification.sms.impl;

import com.flacko.payment.verification.sms.service.SmsPaymentVerificationBuilder;

public interface InitializableSmsPaymentVerificationBuilder extends SmsPaymentVerificationBuilder {

    SmsPaymentVerificationBuilder initializeNew();

}
