package com.flacko.payment.method.service.exception;

public class PaymentMethodInvalidBankAccountLastFourDigitsException extends Exception {

    public PaymentMethodInvalidBankAccountLastFourDigitsException(String id, String accountLastFourDigits) {
        super(String.format("Invalid bank account last four digits %s provided for payment method %s",
                accountLastFourDigits, id));
    }

}
