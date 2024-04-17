package com.flacko.payment.verification.receipt.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.multipart.MultipartFile;

public record ReceiptPaymentVerificationRequest(@JsonProperty(FILE) MultipartFile file,
                                                @JsonProperty(PAYMENT_ID) String paymentId) {

    private static final String FILE = "file";
    private static final String PAYMENT_ID = "payment_id";

}
