package com.flacko.stats.impl;


import com.flacko.stats.service.EntityType;
import com.flacko.stats.service.Stats;
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

@Entity
@Table(name = "stats")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class StatsPojo implements Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primary_key", nullable = false, updatable = false)
    private Long primaryKey;

    @Column(nullable = false)
    private String id;

    @Column(name = "entity_id", nullable = false)
    private String entityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false)
    private EntityType entityType;

    @Column(name = "today_outgoing_total", nullable = false)
    private BigDecimal todayOutgoingTotal;

    @Column(name = "today_incoming_total", nullable = false)
    private BigDecimal todayIncomingTotal;

    @Column(name = "all_time_outgoing_total", nullable = false)
    private BigDecimal allTimeOutgoingTotal;

    @Column(name = "all_time_incoming_total", nullable = false)
    private BigDecimal allTimeIncomingTotal;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "updated_date", nullable = false)
    private Instant updatedDate;

    @PrePersist
    public void preUpdate() {
        createdDate = Instant.now();
        updatedDate = createdDate;
    }

}
