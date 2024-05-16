package com.flacko.trader.team.impl;

import com.flacko.common.country.Country;
import com.flacko.trader.team.service.TraderTeam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Entity
@Table(name = "trader_teams")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TraderTeamPojo implements TraderTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primary_key", nullable = false)
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

    @Column(name = "leader_id", nullable = false)
    private String leaderId;

    @Column(name = "trader_incoming_fee_rate", nullable = false, precision = 6, scale = 5)
    private BigDecimal traderIncomingFeeRate;

    @Column(name = "trader_outgoing_fee_rate", nullable = false, precision = 6, scale = 5)
    private BigDecimal traderOutgoingFeeRate;

    @Column(name = "leader_incoming_fee_rate", nullable = false, precision = 6, scale = 5)
    private BigDecimal leaderIncomingFeeRate;

    @Column(name = "leader_outgoing_fee_rate", nullable = false, precision = 6, scale = 5)
    private BigDecimal leaderOutgoingFeeRate;

    @Column(name = "online", nullable = false)
    private boolean online;

    @Column(name = "kicked_out", nullable = false)
    private boolean kickedOut;

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

