package com.flacko.payment.verification.receipt.service;

import com.flacko.common.exception.IncomingPaymentNotFoundException;
import com.flacko.common.exception.OutgoingPaymentNotFoundException;
import com.flacko.common.exception.ReceiptPaymentVerificationNotFoundException;
import com.flacko.payment.verification.receipt.service.exception.*;
import org.springframework.web.multipart.MultipartFile;

public interface ReceiptPaymentVerificationService {

    ReceiptPaymentVerificationListBuilder list();

    ReceiptPaymentVerification get(String id) throws ReceiptPaymentVerificationNotFoundException;

    ReceiptPaymentVerification verify(MultipartFile file, String paymentId)
            throws ReceiptPaymentVerificationRequestValidationException, ReceiptPaymentVerificationFailedException, ReceiptPaymentVerificationCurrencyNotSupportedException, IncomingPaymentNotFoundException, ReceiptPaymentVerificationInvalidCardLastFourDigitsException, ReceiptPaymentVerificationMissingRequiredAttributeException, ReceiptPaymentVerificationInvalidAmountException, ReceiptPaymentVerificationUnexpectedAmountException, OutgoingPaymentNotFoundException;

}
