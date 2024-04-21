package com.flacko.payment.verification.sms.exception;

import java.math.BigDecimal;

public class SmsPaymentVerificationUnexpectedAmountException extends Exception {

    public SmsPaymentVerificationUnexpectedAmountException(String id, String paymentId, BigDecimal expectedAmount,
                                                           BigDecimal actualAmount) {
        super(String.format("SMS payment verification %s failed for payment %s. " +
                        "The actual amount received was %s, which does not match the expected amount of %s",
                id, paymentId, actualAmount, expectedAmount));
    }

}
