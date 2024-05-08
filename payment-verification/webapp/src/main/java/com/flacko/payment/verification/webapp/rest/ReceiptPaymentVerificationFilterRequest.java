package com.flacko.payment.verification.webapp.rest;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public record ReceiptPaymentVerificationFilterRequest(@RequestParam(PAYMENT_ID) Optional<String> paymentId,
                                                      @RequestParam(name = LIMIT, defaultValue = "10") int limit,
                                                      @RequestParam(name = OFFSET, defaultValue = "0") int offset) {

    private static final String PAYMENT_ID = "payment_id";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

}
