package com.flacko.payment.impl.outgoing;

import com.flacko.common.currency.Currency;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.PaymentMethodNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.id.IdGenerator;
import com.flacko.common.state.PaymentState;
import com.flacko.merchant.service.MerchantService;
import com.flacko.payment.method.service.PaymentMethodService;
import com.flacko.payment.service.outgoing.OutgoingPayment;
import com.flacko.payment.service.outgoing.OutgoingPaymentBuilder;
import com.flacko.payment.service.outgoing.exception.OutgoingPaymentIllegalStateTransitionException;
import com.flacko.payment.service.outgoing.exception.OutgoingPaymentInvalidAmountException;
import com.flacko.payment.service.outgoing.exception.OutgoingPaymentMissingRequiredAttributeException;
import com.flacko.trader.team.service.TraderTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class OutgoingPaymentBuilderImpl implements InitializableOutgoingPaymentBuilder {

    private final OutgoingPaymentRepository outgoingPaymentRepository;
    private final MerchantService merchantService;
    private final TraderTeamService traderTeamService;
    private final PaymentMethodService paymentMethodService;

    private OutgoingPaymentPojo.OutgoingPaymentPojoBuilder pojoBuilder;
    private String id;
    private PaymentState currentState;

    @Override
    public OutgoingPaymentBuilder initializeNew() {
        id = new IdGenerator().generateId();
        currentState = PaymentState.INITIATED;
        pojoBuilder = OutgoingPaymentPojo.builder()
                .id(id)
                .currentState(currentState);
        return this;
    }

    @Override
    public OutgoingPaymentBuilder initializeExisting(OutgoingPayment existingOutgoingPayment) {
        pojoBuilder = OutgoingPaymentPojo.builder()
                .primaryKey(existingOutgoingPayment.getPrimaryKey())
                .id(existingOutgoingPayment.getId())
                .merchantId(existingOutgoingPayment.getMerchantId())
                .traderTeamId(existingOutgoingPayment.getTraderTeamId())
                .paymentMethodId(existingOutgoingPayment.getPaymentMethodId())
                .amount(existingOutgoingPayment.getAmount())
                .currency(existingOutgoingPayment.getCurrency())
                .currentState(existingOutgoingPayment.getCurrentState())
                .createdDate(existingOutgoingPayment.getCreatedDate())
                .updatedDate(Instant.now());
        id = existingOutgoingPayment.getId();
        currentState = existingOutgoingPayment.getCurrentState();
        return this;
    }

    @Override
    public OutgoingPaymentBuilder withMerchantId(String merchantId) {
        pojoBuilder.merchantId(merchantId);
        return this;
    }

    @Override
    public OutgoingPaymentBuilder withTraderTeamId(String traderTeamId) {
        pojoBuilder.traderTeamId(traderTeamId);
        return this;
    }

    @Override
    public OutgoingPaymentBuilder withPaymentMethodId(String paymentMethodId) {
        pojoBuilder.paymentMethodId(paymentMethodId);
        return this;
    }

    @Override
    public OutgoingPaymentBuilder withAmount(BigDecimal amount) {
        pojoBuilder.amount(amount);
        return this;
    }

    @Override
    public OutgoingPaymentBuilder withCurrency(Currency currency) {
        pojoBuilder.currency(currency);
        return this;
    }

    @Override
    public OutgoingPaymentBuilder withState(PaymentState newState)
            throws OutgoingPaymentIllegalStateTransitionException {
        if (!currentState.canChangeTo(newState)) {
            throw new OutgoingPaymentIllegalStateTransitionException(id, currentState, newState);
        }
        pojoBuilder.currentState(newState);
        return this;
    }

    @Override
    public OutgoingPayment build() throws OutgoingPaymentMissingRequiredAttributeException,
            TraderTeamNotFoundException, PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException,
            MerchantNotFoundException {
        OutgoingPaymentPojo payment = pojoBuilder.build();
        validate(payment);
        outgoingPaymentRepository.save(payment);
        return payment;
    }

    private void validate(OutgoingPaymentPojo pojo) throws OutgoingPaymentMissingRequiredAttributeException,
            MerchantNotFoundException, TraderTeamNotFoundException, PaymentMethodNotFoundException,
            OutgoingPaymentInvalidAmountException {
        if (pojo.getId() == null || pojo.getId().isBlank()) {
            throw new OutgoingPaymentMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getMerchantId() == null || pojo.getMerchantId().isBlank()) {
            throw new OutgoingPaymentMissingRequiredAttributeException("merchantId", Optional.of(pojo.getId()));
        } else {
            merchantService.get(pojo.getMerchantId());
        }
        if (pojo.getTraderTeamId() == null || pojo.getTraderTeamId().isBlank()) {
            throw new OutgoingPaymentMissingRequiredAttributeException("traderTeamId", Optional.of(pojo.getId()));
        } else {
            traderTeamService.get(pojo.getTraderTeamId());
        }
        if (pojo.getPaymentMethodId() == null || pojo.getPaymentMethodId().isBlank()) {
            throw new OutgoingPaymentMissingRequiredAttributeException("paymentMethodId", Optional.of(pojo.getId()));
        } else {
            paymentMethodService.get(pojo.getPaymentMethodId());
        }
        if (pojo.getAmount() == null) {
            throw new OutgoingPaymentMissingRequiredAttributeException("amount", Optional.of(pojo.getId()));
        } else if (pojo.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new OutgoingPaymentInvalidAmountException(pojo.getId());
        }
        if (pojo.getCurrency() == null) {
            throw new OutgoingPaymentMissingRequiredAttributeException("currency", Optional.of(pojo.getId()));
        }
        if (pojo.getCurrentState() == null) {
            throw new OutgoingPaymentMissingRequiredAttributeException("currentState", Optional.of(pojo.getId()));
        }
    }

}
