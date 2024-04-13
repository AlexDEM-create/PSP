package com.flacko.terminal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Optional;

@Entity(name = "terminals")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TerminalPojo implements Terminal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primary_key", nullable = false)
    private Long primaryKey;

    @Column(nullable = false)
    private String id;

    // make foreign key to traders table
    @Column(name = "trader_id", nullable = false)
    private String traderId;

    @Column(nullable = false)
    private boolean verified = false;

    @Column
    private String model;

    @Column(name = "operating_system")
    private String operatingSystem;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate = Instant.now();

    @Column(name = "updated_date", nullable = false)
    private Instant updatedDate = createdDate;

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

}
