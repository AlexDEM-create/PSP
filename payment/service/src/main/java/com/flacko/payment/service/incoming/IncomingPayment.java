package com.flacko.payment.service.incoming;

import com.flacko.payment.service.Payment;

public interface IncomingPayment extends Payment {

    String getPaymentMethodId();

}
