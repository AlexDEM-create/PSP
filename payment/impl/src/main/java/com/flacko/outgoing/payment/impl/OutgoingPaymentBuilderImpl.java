package com.flacko.outgoing.payment.impl;

import com.flacko.card.service.CardService;
import com.flacko.common.currency.Currency;
import com.flacko.common.exception.CardNotFoundException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.id.IdGenerator;
import com.flacko.common.state.PaymentState;
import com.flacko.merchant.service.MerchantService;
import com.flacko.payment.service.Payment;
import com.flacko.payment.service.OutgoingPaymentBuilder;
import com.flacko.payment.service.PaymentDirection;
import com.flacko.payment.service.exception.PaymentIllegalStateTransitionException;
import com.flacko.payment.service.exception.PaymentInvalidAmountException;
import com.flacko.payment.service.exception.PaymentMissingRequiredAttributeException;
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
    private final CardService cardService;

    private OutgoingPaymentPojo.PaymentPojoBuilder pojoBuilder;
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
    public OutgoingPaymentBuilder initializeExisting(Payment existingPayment) {
        pojoBuilder = OutgoingPaymentPojo.builder()
                .primaryKey(existingPayment.getPrimaryKey())
                .id(existingPayment.getId())
                .merchantId(existingPayment.getMerchantId())
                .traderTeamId(existingPayment.getTraderTeamId())
                .cardId(existingPayment.getCardId())
                .amount(existingPayment.getAmount())
                .currency(existingPayment.getCurrency())
                .direction(existingPayment.getDirection())
                .currentState(existingPayment.getCurrentState())
                .createdDate(existingPayment.getCreatedDate())
                .updatedDate(Instant.now());
        id = existingPayment.getId();
        currentState = existingPayment.getCurrentState();
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
    public OutgoingPaymentBuilder withCardId(String cardId) {
        pojoBuilder.cardId(cardId);
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
    public OutgoingPaymentBuilder withDirection(PaymentDirection direction) {
        pojoBuilder.direction(direction);
        return this;
    }

    @Override
    public OutgoingPaymentBuilder withState(PaymentState newState) throws PaymentIllegalStateTransitionException {
        if (!currentState.canChangeTo(newState)) {
            throw new PaymentIllegalStateTransitionException(id, currentState, newState);
        }
        pojoBuilder.currentState(newState);
        return this;
    }

    @Override
    public Payment build() throws PaymentMissingRequiredAttributeException, TraderTeamNotFoundException,
            MerchantNotFoundException, CardNotFoundException, PaymentInvalidAmountException {
        OutgoingPaymentPojo payment = pojoBuilder.build();
        validate(payment);
        outgoingPaymentRepository.save(payment);
        return payment;
    }

    private void validate(OutgoingPaymentPojo pojo) throws PaymentMissingRequiredAttributeException, MerchantNotFoundException,
            TraderTeamNotFoundException, CardNotFoundException, PaymentInvalidAmountException {
        if (pojo.getId() == null || pojo.getId().isBlank()) {
            throw new PaymentMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getMerchantId() == null || pojo.getMerchantId().isBlank()) {
            throw new PaymentMissingRequiredAttributeException("merchantId", Optional.of(pojo.getId()));
        } else {
            merchantService.get(pojo.getMerchantId());
        }
        if (pojo.getTraderTeamId() == null || pojo.getTraderTeamId().isBlank()) {
            throw new PaymentMissingRequiredAttributeException("traderTeamId", Optional.of(pojo.getId()));
        } else {
            traderTeamService.get(pojo.getTraderTeamId());
        }
        if (pojo.getCardId() == null || pojo.getCardId().isBlank()) {
            throw new PaymentMissingRequiredAttributeException("cardId", Optional.of(pojo.getId()));
        } else {
            cardService.get(pojo.getCardId());
        }
        if (pojo.getAmount() == null) {
            throw new PaymentMissingRequiredAttributeException("amount", Optional.of(pojo.getId()));
        } else if (pojo.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new PaymentInvalidAmountException(pojo.getId());
        }
        if (pojo.getCurrency() == null) {
            throw new PaymentMissingRequiredAttributeException("currency", Optional.of(pojo.getId()));
        }
        if (pojo.getDirection() == null) {
            throw new PaymentMissingRequiredAttributeException("direction", Optional.of(pojo.getId()));
        }
        if (pojo.getCurrentState() == null) {
            throw new PaymentMissingRequiredAttributeException("currentState", Optional.of(pojo.getId()));
        }
    }

}
