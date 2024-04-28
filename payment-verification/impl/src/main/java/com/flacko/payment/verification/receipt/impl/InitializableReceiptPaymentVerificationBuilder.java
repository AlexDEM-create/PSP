package com.flacko.payment.verification.receipt.impl;

import com.flacko.payment.verification.receipt.service.ReceiptPaymentVerificationBuilder;

public interface InitializableReceiptPaymentVerificationBuilder extends ReceiptPaymentVerificationBuilder {

    ReceiptPaymentVerificationBuilder initializeNew();

}
