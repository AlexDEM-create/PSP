package com.flacko.payment.verification.receipt.service;

import java.util.List;

public interface ReceiptPaymentVerificationListBuilder {

    ReceiptPaymentVerificationListBuilder withPaymentId(String paymentId);

    List<ReceiptPaymentVerification> build();

}
