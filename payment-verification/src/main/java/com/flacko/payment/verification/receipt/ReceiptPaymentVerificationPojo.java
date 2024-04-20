package com.flacko.payment.verification.receipt;

import com.flacko.payment.verification.HashMapConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
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
    @Column(name = "payment_id", nullable = false)
    private String paymentId;

    @Column(name = "recipient_full_name", nullable = false)
    private String recipientFullName;

    @Column(name = "recipient_card_last_four_digits", nullable = false)
    private String recipientCardLastFourDigits;

    @Column(name = "sender_full_name", nullable = false)
    private String senderFullName;

    @Column(name = "sender_card_last_four_digits", nullable = false)
    private String senderCardLastFourDigits;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "amount_currency", nullable = false)
    private Currency amountCurrency;

    @Column(nullable = false)
    private BigDecimal commission;

    @Column(name = "commission_currency", nullable = false)
    private Currency commissionCurrency;

    // https://www.baeldung.com/hibernate-persist-json-object
    @Convert(converter = HashMapConverter.class)
    @Column(nullable = false)
    private Map<String, Object> data;

    @Lob
    @Column(name = "uploaded_file", nullable = false, length = MAX_RECEIPT_SIZE)
    private byte[] uploadedFile;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @PrePersist
    protected void onCreate() {
        createdDate = Instant.now();
    }

}
