package com.flacko.payment.verification.sms.impl;

import com.flacko.common.converter.HashMapConverter;
import com.flacko.common.currency.Currency;
import com.flacko.payment.verification.sms.service.SmsPaymentVerification;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "sms_payment_verifications")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SmsPaymentVerificationPojo implements SmsPaymentVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primary_key", nullable = false, updatable = false)
    private Long primaryKey;

    @Column(nullable = false)
    private String id;

    // make foreign key to incoming_payments table
    @Column(name = "incoming_payment_id", nullable = false)
    private String incomingPaymentId;

    @Column(name = "recipient_card_last_four_digits", nullable = false)
    private String recipientCardLastFourDigits;

    @Column(name = "sender_full_name", nullable = false)
    private String senderFullName;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "amount_currency", nullable = false)
    private Currency amountCurrency;

    @Column(nullable = false)
    private String message;

    private String dataJSON;

    // https://www.baeldung.com/hibernate-persist-json-object
    @Convert(converter = HashMapConverter.class)
    @Column(nullable = false)
    private Map<String, Object> data;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @PrePersist
    protected void onCreate() {
        createdDate = Instant.now();
    }

}
