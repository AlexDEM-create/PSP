package com.flacko.payment.verification.sms;

import com.flacko.payment.verification.sms.exception.SmsPaymentVerificationMissingRequiredAttributeException;

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

    SmsPaymentVerification build() throws SmsPaymentVerificationMissingRequiredAttributeException;

}
