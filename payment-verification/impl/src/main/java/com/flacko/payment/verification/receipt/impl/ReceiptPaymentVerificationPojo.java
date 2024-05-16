package com.flacko.payment.verification.receipt.impl;

import com.flacko.common.converter.HashMapConverter;
import com.flacko.payment.verification.receipt.service.ReceiptPaymentVerification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "receipt_payment_verifications")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptPaymentVerificationPojo implements ReceiptPaymentVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primary_key", nullable = false)
    private Long primaryKey;

    @Column(nullable = false)
    private String id;

    // make foreign key to payments table
    @Column(name = "outgoing_payment_id", nullable = false)
    private String outgoingPaymentId;

    // https://www.baeldung.com/hibernate-persist-json-object
    @Convert(converter = HashMapConverter.class)
    @Column(nullable = false, length = 4096)
    private Map<String, Object> data;

//    @Lob
    @Column(name = "uploaded_file", nullable = false, length = MAX_RECEIPT_SIZE)
    private byte[] uploadedFile;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @PrePersist
    protected void onCreate() {
        createdDate = Instant.now();
    }

}
