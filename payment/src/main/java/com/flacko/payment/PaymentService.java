package com.flacko.payment;

import com.flacko.payment.exception.PaymentNotFoundException;

import java.util.List;

public interface PaymentService {

    PaymentBuilder create();

    PaymentBuilder update(String id) throws PaymentNotFoundException;

    List<Payment> list();

    Payment get(String id) throws PaymentNotFoundException;

}
