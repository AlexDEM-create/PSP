package com.flacko.payment.verification.receipt.service;

import java.time.Instant;
import java.util.Map;

public interface ReceiptPaymentVerification {

    int MAX_RECEIPT_SIZE = 256 * 1024;

    Long getPrimaryKey();

    String getId();

    String getOutgoingPaymentId();

    Map<String, Object> getData();

    Instant getCreatedDate();

}
