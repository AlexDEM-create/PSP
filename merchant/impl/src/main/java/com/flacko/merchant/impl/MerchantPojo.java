package com.flacko.merchant.impl;

import com.flacko.common.country.Country;
import com.flacko.merchant.service.Merchant;
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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Entity
@Table(name = "merchants")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MerchantPojo implements Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primary_key", nullable = false, updatable = false)
    private Long primaryKey;

    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Country country;

    @Column(name = "incoming_fee_rate", nullable = false, precision = 6, scale = 5)
    private BigDecimal incomingFeeRate;

    @Column(name = "outgoing_fee_rate", nullable = false, precision = 6, scale = 5)
    private BigDecimal outgoingFeeRate;

    @Column(name = "outgoing_traffic_stopped", nullable = false)
    private boolean outgoingTrafficStopped;

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
        outgoingTrafficStopped = false;
        createdDate = Instant.now();
        updatedDate = createdDate;
    }

}

