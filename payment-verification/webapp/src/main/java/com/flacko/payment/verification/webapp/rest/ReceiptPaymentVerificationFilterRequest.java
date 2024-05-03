package com.flacko.payment.verification.webapp.rest;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public record ReceiptPaymentVerificationFilterRequest(@RequestParam(PAYMENT_ID) Optional<String> paymentId) {

    private static final String PAYMENT_ID = "payment_id";

}
