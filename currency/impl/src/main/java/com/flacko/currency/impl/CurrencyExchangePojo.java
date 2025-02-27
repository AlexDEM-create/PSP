
package com.flacko.currency.impl;

import com.flacko.common.currency.Currency;
import com.flacko.currency.service.CurrencyExchange;
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
@Table(name = "currency_exchanges")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyExchangePojo implements CurrencyExchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primary_key", nullable = false, updatable = false)
    private Long primaryKey;

    @Column(nullable = false)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_currency", nullable = false)
    private Currency sourceCurrency;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_currency", nullable = false)
    private Currency targetCurrency;

    @Column(name = "buy_exchange_rate", nullable = false)
    private BigDecimal buyExchangeRate;

    @Column(name = "sell_exchange_rate", nullable = false)
    private BigDecimal sellExchangeRate;

    @Column(name = "updated_date", nullable = false)
    private Instant updatedDate;

    @PrePersist
    protected void onCreate() {
        updatedDate = Instant.now();
    }

}
