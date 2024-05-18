package com.flacko.payment.impl.incoming;

import com.flacko.common.currency.Currency;
import com.flacko.common.state.PaymentState;
import com.flacko.payment.service.incoming.IncomingPayment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "incoming_payments")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class IncomingPaymentPojo implements IncomingPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primary_key", nullable = false)
    private Long primaryKey;

    @Column(nullable = false)
    private String id;

    // make foreign key to merchants table
    @Column(name = "merchant_id", nullable = false)
    private String merchantId;

    // make foreign key to traders table
    @Column(name = "trader_team_id", nullable = false)
    private String traderTeamId;

    // make foreign key to payment_methods table
    @Column(name = "payment_method_id", nullable = false)
    private String paymentMethodId;

    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_state", nullable = false)
    private PaymentState currentState;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "updated_date", nullable = false)
    private Instant updatedDate;

    @PrePersist
    protected void onCreate() {
        createdDate = Instant.now();
        updatedDate = createdDate;
    }

}
