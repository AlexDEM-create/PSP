package com.flacko.payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

@Entity(name = "payments")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPojo implements Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primary_key", nullable = false)
    private Long primaryKey;

    @Column(nullable = false)
    private String id;

    // make foreign key to merchants table
    @Column(name = "merchant_id", nullable = false)
    private String merchantId;

    // make foreign key to traders table
    @Column(name = "trader_id", nullable = false)
    private String traderId;

    // make foreign key to cards table
    @Column(name = "card_id", nullable = false)
    private String cardId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentDirection direction;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_state", nullable = false)
    private PaymentState currentState;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "updated_date", nullable = false)
    private Instant updatedDate;

    @PrePersist
    protected void onCreate() {
        createdDate = Instant.now();
        updatedDate = createdDate;
    }

}
