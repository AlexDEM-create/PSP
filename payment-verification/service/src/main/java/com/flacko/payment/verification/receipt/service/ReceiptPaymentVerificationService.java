package com.flacko.payment.verification.receipt.service;

import com.flacko.common.exception.ReceiptPaymentVerificationNotFoundException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationFailedException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationRequestValidationException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReceiptPaymentVerificationService {

    List<ReceiptPaymentVerification> list();

    ReceiptPaymentVerification get(String id) throws ReceiptPaymentVerificationNotFoundException;

    ReceiptPaymentVerification verify(MultipartFile file, String paymentId)
            throws ReceiptPaymentVerificationRequestValidationException, ReceiptPaymentVerificationFailedException;

}
