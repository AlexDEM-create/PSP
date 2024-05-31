package com.flacko.payment.impl.outgoing;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.common.payment.RecipientPaymentMethodType;
import com.flacko.common.state.PaymentState;
import com.flacko.payment.service.outgoing.OutgoingPayment;
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
import java.util.Optional;

@Entity
@Table(name = "outgoing_payments")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OutgoingPaymentPojo implements OutgoingPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primary_key", nullable = false, updatable = false)
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

    @Column(nullable = false)
    private String recipient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Bank bank;

    @Enumerated(EnumType.STRING)
    @Column(name = "recipient_payment_method_type", nullable = false)
    private RecipientPaymentMethodType recipientPaymentMethodType;

    @Column(name = "partner_payment_id", nullable = false)
    private String partnerPaymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_state", nullable = false)
    private PaymentState currentState;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "updated_date", nullable = false)
    private Instant updatedDate;

    public Optional<String> getPaymentMethodId() {
        return Optional.ofNullable(paymentMethodId);
    }

    @PrePersist
    protected void onCreate() {
        createdDate = Instant.now();
        updatedDate = createdDate;
    }

}
