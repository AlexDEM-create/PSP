package com.flacko.payment.verification.sms.service.exception;

import java.math.BigDecimal;

public class SmsPaymentVerificationUnexpectedAmountException extends Exception {

    public SmsPaymentVerificationUnexpectedAmountException(String id, String incomingPaymentId,
                                                           BigDecimal expectedAmount, BigDecimal actualAmount) {
        super(String.format("SMS payment verification %s failed for incoming payment %s. " +
                        "The actual amount received was %s, which does not match the expected amount of %s",
                id, incomingPaymentId, actualAmount, expectedAmount));
    }

}
