package com.flacko.payment.verification.sms.service;

import com.flacko.common.exception.PaymentNotFoundException;
import com.flacko.payment.verification.sms.service.exception.SmsPaymentVerificationInvalidCardLastFourDigitsException;
import com.flacko.payment.verification.sms.service.exception.SmsPaymentVerificationMissingRequiredAttributeException;
import com.flacko.payment.verification.sms.service.exception.SmsPaymentVerificationUnexpectedAmountException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

public interface SmsPaymentVerificationBuilder {

    SmsPaymentVerificationBuilder withPaymentId(String paymentId);

    SmsPaymentVerificationBuilder withRecipientCardLastFourDigits(String recipientCardLastFourDigits);

    SmsPaymentVerificationBuilder withSenderFullName(String senderFullName);

    SmsPaymentVerificationBuilder withAmount(BigDecimal amount);

    SmsPaymentVerificationBuilder withAmountCurrency(Currency amountCurrency);

    SmsPaymentVerificationBuilder withMessage(String message);

    SmsPaymentVerificationBuilder withData(Map<String, Object> data);

    SmsPaymentVerification build() throws SmsPaymentVerificationMissingRequiredAttributeException,
            SmsPaymentVerificationInvalidCardLastFourDigitsException, PaymentNotFoundException,
            SmsPaymentVerificationUnexpectedAmountException;

}
