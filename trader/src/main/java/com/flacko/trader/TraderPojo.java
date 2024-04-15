package com.flacko.trader;

import com.flacko.trader.exception.TraderMissingRequiredAttributeException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Entity(name = "traders")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TraderPojo implements Trader {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primary_key", nullable = false)
    private Long primaryKey;

    @Column(nullable = false)
    private String id;


    @Column(nullable = false)
    private String name;

    @Column(name = "userid", nullable = false)
    private String userId;

    @Column(name = "trader_team_id")
    private String traderTeamId;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate = Instant.now();

    @Column(name = "updated_date", nullable = false)
    private Instant updatedDate = createdDate;

    @Override
    public String getUserId() {
        return null;
    }

    @Override
    public String getTraderTeamId() {
        return null;
    }
}

