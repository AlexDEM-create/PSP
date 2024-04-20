package com.flacko.payment.verification.receipt.exception;

public class ReceiptPaymentVerificationInvalidAmountException extends Exception {

    public ReceiptPaymentVerificationInvalidAmountException(String amount) {
        super(String.format("Error parsing amount %s", amount));
    }

}
