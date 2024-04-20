package com.flacko.payment.verification.receipt.exception;

public class ReceiptPaymentVerificationCurrencyNotSupportedException extends Exception {

    public ReceiptPaymentVerificationCurrencyNotSupportedException(String currency) {
        super(String.format("Currency %s is not supported", currency));
    }

}
