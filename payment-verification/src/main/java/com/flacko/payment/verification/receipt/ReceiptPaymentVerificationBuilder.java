package com.flacko.payment.verification.receipt;

import com.flacko.payment.verification.receipt.exception.ReceiptPaymentVerificationMissingRequiredAttributeException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

public interface ReceiptPaymentVerificationBuilder {

    ReceiptPaymentVerificationBuilder withPaymentId(String paymentId);

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

    ReceiptPaymentVerification build() throws ReceiptPaymentVerificationMissingRequiredAttributeException;

}
