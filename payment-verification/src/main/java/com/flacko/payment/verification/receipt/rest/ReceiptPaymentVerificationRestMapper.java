package com.flacko.payment.verification.receipt.rest;

import com.flacko.payment.verification.receipt.ReceiptPaymentVerification;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class ReceiptPaymentVerificationRestMapper {

    ReceiptPaymentVerificationResponse mapModelToResponse(ReceiptPaymentVerification receiptPaymentVerification) {
        // add timezone from authorization
        return new ReceiptPaymentVerificationResponse(receiptPaymentVerification.getId(),
                receiptPaymentVerification.getPaymentId(),
                receiptPaymentVerification.getRecipientFullName(),
                receiptPaymentVerification.getRecipientCardLastFourDigits(),
                receiptPaymentVerification.getSenderFullName(),
                receiptPaymentVerification.getSenderCardLastFourDigits(),
                receiptPaymentVerification.getAmount(),
                receiptPaymentVerification.getAmountCurrency(),
                receiptPaymentVerification.getCommission(),
                receiptPaymentVerification.getCommissionCurrency(),
                receiptPaymentVerification.getData(),
                receiptPaymentVerification.getCreatedDate().atZone(ZoneId.systemDefault()));
    }

}
