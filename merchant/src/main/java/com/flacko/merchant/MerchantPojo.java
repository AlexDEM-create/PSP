package com.flacko.merchant;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Optional;

@Entity(name = "merchants")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MerchantPojo implements Merchant{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primary_key", nullable = false)
    private Long primaryKey;

    @Column(nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean verified = false;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate = Instant.now();

    @Column(name = "updated_date", nullable = false)
    private Instant updatedDate = createdDate;

    @Column(name = "deleted_date")
    private Instant deletedDate;

    public Optional<String> getUserId() {
        return Optional.ofNullable(userId);
    }
    public Optional<Instant> getDeletedDate() {
        return Optional.ofNullable(deletedDate);
    }
}

