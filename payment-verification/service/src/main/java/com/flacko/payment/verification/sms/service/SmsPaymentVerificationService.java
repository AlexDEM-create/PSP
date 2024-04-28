package com.flacko.payment.verification.sms.service;

import com.flacko.common.exception.SmsPaymentVerificationNotFoundException;

import java.util.List;

public interface SmsPaymentVerificationService {

    SmsPaymentVerificationBuilder create();

    List<SmsPaymentVerification> list();

    SmsPaymentVerification get(String id) throws SmsPaymentVerificationNotFoundException;

}
