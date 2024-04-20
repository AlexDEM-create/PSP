package com.flacko.payment.verification.bank.pattern;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Optional;

@Entity(name = "bank_patterns")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BankPatternPojo implements BankPattern {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primary_key", nullable = false)
    private Long primaryKey;

    @Column(nullable = false)
    private String id;

    @Column(nullable = false, length = 4000)
    private String pattern;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BankPatternType type;

    @Column(name = "bank_id", nullable = false)
    private String bankId;

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
