package com.flacko.payment.impl;

import com.flacko.card.service.CardService;
import com.flacko.common.currency.Currency;
import com.flacko.common.exception.CardNotFoundException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.id.IdGenerator;
import com.flacko.common.state.PaymentState;
import com.flacko.merchant.service.MerchantService;
import com.flacko.payment.service.Payment;
import com.flacko.payment.service.PaymentBuilder;
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
public class PaymentBuilderImpl implements InitializablePaymentBuilder {

    private final PaymentRepository paymentRepository;
    private final MerchantService merchantService;
    private final TraderTeamService traderTeamService;
    private final CardService cardService;

    private PaymentPojo.PaymentPojoBuilder pojoBuilder;
    private String id;
    private PaymentState currentState;

    @Override
    public PaymentBuilder initializeNew() {
        id = new IdGenerator().generateId();
        currentState = PaymentState.INITIATED;
        pojoBuilder = PaymentPojo.builder()
                .id(id)
                .currentState(currentState);
        return this;
    }

    @Override
    public PaymentBuilder initializeExisting(Payment existingPayment) {
        pojoBuilder = PaymentPojo.builder()
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
    public PaymentBuilder withMerchantId(String merchantId) {
        pojoBuilder.merchantId(merchantId);
        return this;
    }

    @Override
    public PaymentBuilder withTraderTeamId(String traderTeamId) {
        pojoBuilder.traderTeamId(traderTeamId);
        return this;
    }

    @Override
    public PaymentBuilder withCardId(String cardId) {
        pojoBuilder.cardId(cardId);
        return this;
    }

    @Override
    public PaymentBuilder withAmount(BigDecimal amount) {
        pojoBuilder.amount(amount);
        return this;
    }

    @Override
    public PaymentBuilder withCurrency(Currency currency) {
        pojoBuilder.currency(currency);
        return this;
    }

    @Override
    public PaymentBuilder withDirection(PaymentDirection direction) {
        pojoBuilder.direction(direction);
        return this;
    }

    @Override
    public PaymentBuilder withState(PaymentState newState) throws PaymentIllegalStateTransitionException {
        if (!currentState.canChangeTo(newState)) {
            throw new PaymentIllegalStateTransitionException(id, currentState, newState);
        }
        pojoBuilder.currentState(newState);
        return this;
    }

    @Override
    public Payment build() throws PaymentMissingRequiredAttributeException, TraderTeamNotFoundException,
            MerchantNotFoundException, CardNotFoundException, PaymentInvalidAmountException {
        PaymentPojo payment = pojoBuilder.build();
        validate(payment);
        paymentRepository.save(payment);
        return payment;
    }

    private void validate(PaymentPojo pojo) throws PaymentMissingRequiredAttributeException, MerchantNotFoundException,
            TraderTeamNotFoundException, CardNotFoundException, PaymentInvalidAmountException {
        if (pojo.getId() == null || pojo.getId().isEmpty()) {
            throw new PaymentMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getMerchantId() == null || pojo.getMerchantId().isEmpty()) {
            throw new PaymentMissingRequiredAttributeException("merchantId", Optional.of(pojo.getId()));
        } else {
            merchantService.get(pojo.getMerchantId());
        }
        if (pojo.getTraderTeamId() == null || pojo.getTraderTeamId().isEmpty()) {
            throw new PaymentMissingRequiredAttributeException("traderTeamId", Optional.of(pojo.getId()));
        } else {
            traderTeamService.get(pojo.getTraderTeamId());
        }
        if (pojo.getCardId() == null || pojo.getCardId().isEmpty()) {
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
