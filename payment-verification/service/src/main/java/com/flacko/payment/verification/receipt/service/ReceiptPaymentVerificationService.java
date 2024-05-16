package com.flacko.payment.verification.receipt.service;

import com.flacko.common.exception.*;
import com.flacko.payment.verification.receipt.service.exception.*;
import org.springframework.web.multipart.MultipartFile;

public interface ReceiptPaymentVerificationService {

    ReceiptPaymentVerificationListBuilder list();

    ReceiptPaymentVerification getByOutgoingPaymentId(String outgoingPaymentId)
            throws ReceiptPaymentVerificationNotFoundException;

    ReceiptPaymentVerification verify(MultipartFile file, String outgoingPaymentId)
            throws ReceiptPaymentVerificationRequestValidationException, ReceiptPaymentVerificationFailedException,
            ReceiptPaymentVerificationCurrencyNotSupportedException, IncomingPaymentNotFoundException,
            ReceiptPaymentVerificationMissingRequiredAttributeException,
            ReceiptPaymentVerificationInvalidAmountException, ReceiptPaymentVerificationUnexpectedAmountException,
            OutgoingPaymentNotFoundException, PaymentMethodNotFoundException, TraderTeamNotFoundException,
            BalanceNotFoundException, MerchantNotFoundException, BalanceMissingRequiredAttributeException,
            OutgoingPaymentIllegalStateTransitionException, OutgoingPaymentMissingRequiredAttributeException,
            OutgoingPaymentInvalidAmountException;

}
