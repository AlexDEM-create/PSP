package com.flacko.payment.verification.receipt.service;

import com.flacko.common.currency.Currency;
import com.flacko.common.exception.IncomingPaymentNotFoundException;
import com.flacko.common.exception.OutgoingPaymentNotFoundException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationInvalidCardLastFourDigitsException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationMissingRequiredAttributeException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationUnexpectedAmountException;

import java.math.BigDecimal;
import java.util.Map;

public interface ReceiptPaymentVerificationBuilder {

    ReceiptPaymentVerificationBuilder withOutgoingPaymentId(String outgoingPaymentId);

    ReceiptPaymentVerificationBuilder withRecipientFullName(String recipientFullName);

    ReceiptPaymentVerificationBuilder withRecipientCardLastFourDigits(String recipientCardLastFourDigits);

    ReceiptPaymentVerificationBuilder withSenderFullName(String senderFullName);

    ReceiptPaymentVerificationBuilder withSenderCardLastFourDigits(String senderCardLastFourDigits);

    ReceiptPaymentVerificationBuilder withAmount(BigDecimal amount);

    ReceiptPaymentVerificationBuilder withAmountCurrency(Currency amountCurrency);

    ReceiptPaymentVerificationBuilder withCommission(BigDecimal commission);

    ReceiptPaymentVerificationBuilder withCommissionCurrency(Currency commissionCurrency);

    ReceiptPaymentVerificationBuilder withData(Map<String, Object> data);

    ReceiptPaymentVerificationBuilder withUploadedFile(byte[] uploadedFile);

    ReceiptPaymentVerification build() throws ReceiptPaymentVerificationMissingRequiredAttributeException,
            IncomingPaymentNotFoundException, ReceiptPaymentVerificationUnexpectedAmountException,
            ReceiptPaymentVerificationInvalidCardLastFourDigitsException, OutgoingPaymentNotFoundException;

}
