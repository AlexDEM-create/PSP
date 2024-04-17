package com.flacko.card;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Optional;

@Entity(name = "cards")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CardPojo implements Card{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primary_key", nullable = false)
    private Long primaryKey;

    @Column(nullable = false)
    private String cardId;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "card_name", nullable = false)
    private String cardName;

    @Column(name = "card_date", nullable = false)
    private Instant cardDate;

    @Column(name = "bank_id")
    private String bankId;

    @Column(nullable = false)
    private boolean isActive = false;

    @Column(name = "trader_id")
    private String traderId;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate = Instant.now();

    @Column(name = "updated_date", nullable = false)
    private Instant updatedDate = createdDate;

    @Column(name = "deleted_date")
    private Instant deletedDate;

    public Optional<String> getTraderId() {
        return Optional.ofNullable(traderId);
    }
    public Optional<Instant> getDeletedDate() {
        return Optional.ofNullable(deletedDate);
    }
}
