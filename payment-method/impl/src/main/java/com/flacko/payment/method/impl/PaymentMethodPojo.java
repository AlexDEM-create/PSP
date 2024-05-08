package com.flacko.payment.method.impl;

import com.flacko.common.currency.Currency;
import com.flacko.payment.method.service.PaymentMethod;
import com.flacko.payment.method.service.PaymentMethodType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Optional;

@Entity
@Table(name = "payment_methods")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodPojo implements PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primary_key", nullable = false)
    private Long primaryKey;

    @Column(nullable = false)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethodType type;

    @Column(nullable = false)
    private String number;

    @Column(name = "holder_name", nullable = false)
    private String holderName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;


    @Column(name = "bank_id", nullable = false)
    private String bankId;

    @Column(name = "trader_team_id", nullable = false)
    private String traderTeamId;

    @Column(name = "terminal_id", nullable = false)
    private String terminalId;

    @Column(nullable = false)
    private boolean busy;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "updated_date", nullable = false)
    private Instant updatedDate;

    @Column(name = "deleted_date")
    private Instant deletedDate;

    public Optional<Instant> getDeletedDate() {
        return Optional.ofNullable(deletedDate);
    }

    @PrePersist
    protected void onCreate() {
        createdDate = Instant.now();
        updatedDate = createdDate;
    }

}
