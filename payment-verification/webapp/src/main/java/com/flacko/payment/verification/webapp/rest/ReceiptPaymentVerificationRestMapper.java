package com.flacko.payment.verification.webapp.rest;

import com.flacko.payment.verification.receipt.service.ReceiptPaymentVerification;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class ReceiptPaymentVerificationRestMapper {

    ReceiptPaymentVerificationResponse mapModelToResponse(ReceiptPaymentVerification receiptPaymentVerification) {
        // add timezone from authorization
        return new ReceiptPaymentVerificationResponse(receiptPaymentVerification.getId(),
                receiptPaymentVerification.getOutgoingPaymentId(),
                receiptPaymentVerification.getData(),
                receiptPaymentVerification.getCreatedDate().atZone(ZoneId.systemDefault()));
    }

}
