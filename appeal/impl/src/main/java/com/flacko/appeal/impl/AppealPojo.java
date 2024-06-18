package com.flacko.appeal.impl;

import com.flacko.appeal.service.Appeal;
import com.flacko.appeal.service.AppealSource;
import com.flacko.appeal.service.AppealState;
import com.flacko.appeal.service.PaymentDirection;
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
@Table(name = "appeals")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AppealPojo implements Appeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primary_key", nullable = false, updatable = false)
    private Long primaryKey;

    @Column(nullable = false)
    private String id;

    @Column(name = "payment_id", nullable = false)
    private String paymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_direction", nullable = false)
    private PaymentDirection paymentDirection;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppealSource source;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_state", nullable = false)
    private AppealState currentState;

    @Column
    private String message;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "updated_date", nullable = false)
    private Instant updatedDate;

    public Optional<String> getMessage() {
        return Optional.ofNullable(message);
    }

    @PrePersist
    protected void onCreate() {
        createdDate = Instant.now();
        updatedDate = createdDate;
    }

}
