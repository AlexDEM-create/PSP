package com.flacko.payment.verification.receipt;

import com.flacko.payment.verification.receipt.exception.ReceiptPaymentVerificationFailedException;
import com.flacko.payment.verification.receipt.exception.ReceiptPaymentVerificationNotFoundException;
import com.flacko.payment.verification.receipt.exception.ReceiptPaymentVerificationRequestValidationException;
import com.flacko.payment.verification.receipt.rest.ReceiptPaymentVerificationRequest;

import java.util.List;

public interface ReceiptPaymentVerificationService {

    List<ReceiptPaymentVerification> list();

    ReceiptPaymentVerification get(String id) throws ReceiptPaymentVerificationNotFoundException;

    ReceiptPaymentVerification verify(ReceiptPaymentVerificationRequest receiptPaymentVerificationRequest) throws ReceiptPaymentVerificationRequestValidationException, ReceiptPaymentVerificationFailedException;

}
