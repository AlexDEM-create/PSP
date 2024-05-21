package com.flacko.reporting.impl;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.flacko.reporting.service.EntityType;
import com.flacko.reporting.service.Stats;

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
    @Column(name = "primary_key", nullable = false)
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
