package com.flacko.terminal.impl;

import com.flacko.terminal.service.Terminal;
import jakarta.persistence.Column;
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

import java.time.Instant;
import java.util.Optional;

@Entity
@Table(name = "terminals")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TerminalPojo implements Terminal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primary_key", nullable = false, updatable = false)
    private Long primaryKey;

    @Column(nullable = false)
    private String id;

    // make foreign key to traders table
    @Column(name = "trader_team_id", nullable = false)
    private String traderTeamId;

    @Column(nullable = false)
    private boolean verified;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private boolean online;

    @Column
    private String model;

    @Column(name = "operating_system")
    private String operatingSystem;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "updated_date", nullable = false)
    private Instant updatedDate;

    @Column(name = "deleted_date")
    private Instant deletedDate;

    public Optional<String> getModel() {
        return Optional.ofNullable(model);
    }

    public Optional<String> getOperatingSystem() {
        return Optional.ofNullable(operatingSystem);
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
