package com.flacko.payment.verification.receipt.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.multipart.MultipartFile;

public record ReceiptPaymentVerificationRequest(@JsonProperty(FILE) MultipartFile file,
                                                @JsonProperty(TRADER_ID) String traderId,
                                                @JsonProperty(PAYMENT_ID) String paymentId,
                                                @JsonProperty(MERCHANT_ID) String merchantId) {

    private static final String FILE = "file";
    private static final String TRADER_ID = "trader_id";
    private static final String PAYMENT_ID = "payment_id";
    private static final String MERCHANT_ID = "merchant_id";

}
