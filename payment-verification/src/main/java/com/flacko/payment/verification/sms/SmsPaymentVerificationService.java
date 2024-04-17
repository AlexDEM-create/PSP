package com.flacko.payment.verification.sms;

import com.flacko.payment.verification.sms.exception.SmsPaymentVerificationNotFoundException;

import java.util.List;

public interface SmsPaymentVerificationService {

    SmsPaymentVerificationBuilder create();

    List<SmsPaymentVerification> list();

    SmsPaymentVerification get(String id) throws SmsPaymentVerificationNotFoundException;

}
