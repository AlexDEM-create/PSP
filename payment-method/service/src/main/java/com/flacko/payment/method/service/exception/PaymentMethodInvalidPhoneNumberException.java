package com.flacko.payment.method.service.exception;

public class PaymentMethodInvalidPhoneNumberException extends Exception {

    public PaymentMethodInvalidPhoneNumberException(String id, String number) {
        super(String.format("Invalid phone number %s provided for payment method %s", number, id));
    }

}
