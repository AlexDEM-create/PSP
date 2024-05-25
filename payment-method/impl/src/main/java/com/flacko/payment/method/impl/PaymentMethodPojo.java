package com.flacko.payment.method.impl;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.payment.method.service.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
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

    @Column(nullable = false)
    private String number;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Bank bank;

    @Column(name = "trader_team_id", nullable = false)
    private String traderTeamId;

    @Column(name = "terminal_id")
    private String terminalId;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private boolean busy;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "updated_date", nullable = false)
    private Instant updatedDate;

    @Column(name = "deleted_date")
    private Instant deletedDate;

    public Optional<String> getTerminalId() {
        return Optional.ofNullable(terminalId);
    }

    public Optional<Instant> getDeletedDate() {
        return Optional.ofNullable(deletedDate);
    }

    @PrePersist
    protected void onCreate() {
        createdDate = Instant.now();
        updatedDate = createdDate;
    }

}
