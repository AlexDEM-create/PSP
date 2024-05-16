package com.flacko.payment.verification.receipt.service;

import com.flacko.common.exception.IncomingPaymentNotFoundException;
import com.flacko.common.exception.OutgoingPaymentNotFoundException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationMissingRequiredAttributeException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationUnexpectedAmountException;

import java.util.Map;

public interface ReceiptPaymentVerificationBuilder {

    ReceiptPaymentVerificationBuilder withOutgoingPaymentId(String outgoingPaymentId);

    ReceiptPaymentVerificationBuilder withData(Map<String, Object> data);

    ReceiptPaymentVerificationBuilder withUploadedFile(byte[] uploadedFile);

    ReceiptPaymentVerification build() throws ReceiptPaymentVerificationMissingRequiredAttributeException,
            IncomingPaymentNotFoundException, ReceiptPaymentVerificationUnexpectedAmountException,
            OutgoingPaymentNotFoundException;

}
