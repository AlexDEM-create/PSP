package com.flacko.payment.verification.receipt.service;

import java.util.List;

public interface ReceiptPaymentVerificationListBuilder {

    ReceiptPaymentVerificationListBuilder withOutgoingPaymentId(String outgoingPaymentId);

    List<ReceiptPaymentVerification> build();

}
