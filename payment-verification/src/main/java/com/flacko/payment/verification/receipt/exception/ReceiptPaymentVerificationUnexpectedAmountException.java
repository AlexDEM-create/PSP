package com.flacko.payment.verification.receipt.exception;

import java.math.BigDecimal;

public class ReceiptPaymentVerificationUnexpectedAmountException extends Exception {

    public ReceiptPaymentVerificationUnexpectedAmountException(String id, String paymentId, BigDecimal expectedAmount,
                                                               BigDecimal actualAmount) {
        super(String.format("Receipt payment verification %s failed for payment %s. " +
                        "The actual amount received was %s, which does not match the expected amount of %s",
                id, paymentId, actualAmount, expectedAmount));
    }

}
