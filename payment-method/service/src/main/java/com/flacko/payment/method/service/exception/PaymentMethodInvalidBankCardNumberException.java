package com.flacko.payment.method.service.exception;

public class PaymentMethodInvalidBankCardNumberException extends Exception {

    public PaymentMethodInvalidBankCardNumberException(String id, String number) {
        super(String.format("Invalid bank card number %s provided for payment method %s", number, id));
    }

}
