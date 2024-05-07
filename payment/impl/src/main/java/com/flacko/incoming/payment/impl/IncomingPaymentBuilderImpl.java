package com.flacko.incoming.payment.impl;

import com.flacko.card.service.CardService;
import com.flacko.common.currency.Currency;
import com.flacko.common.exception.CardNotFoundException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.id.IdGenerator;
import com.flacko.common.state.PaymentState;
import com.flacko.incoming.payment.service.IncomingPaymentBuilder;
import com.flacko.incoming.payment.service.exception.IncomingPaymentIllegalStateTransitionException;
import com.flacko.incoming.payment.service.exception.IncomingPaymentMissingRequiredAttributeException;
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
public class IncomingPaymentBuilderImpl implements InitializableIncomingPaymentBuilder {

    private final IncomingPaymentRepository incomingPaymentRepository;
    private final MerchantService merchantService;
    private final TraderTeamService traderTeamService;
    private final CardService cardService;

    private IncomingPaymentPojo.IncomingPaymentPojoBuilder pojoBuilder;
    private String id;
    private PaymentState currentState;

    @Override
    public IncomingPaymentBuilder initializeNew() {
        id = new IdGenerator().generateId();
        currentState = PaymentState.INITIATED;
        pojoBuilder = IncomingPaymentPojo.builder()
                .id(id)
                .currentState(currentState);
        return this;
    }

    @Override
    public IncomingPaymentBuilder initializeExisting(Payment existingPayment) {
        pojoBuilder = IncomingPaymentPojo.builder()
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
    public IncomingPaymentBuilder withMerchantId(String merchantId) {
        pojoBuilder.merchantId(merchantId);
        return this;
    }

    @Override
    public IncomingPaymentBuilder withTraderTeamId(String traderTeamId) {
        pojoBuilder.traderTeamId(traderTeamId);
        return this;
    }

    @Override
    public IncomingPaymentBuilder withCardId(String cardId) {
        pojoBuilder.cardId(cardId);
        return this;
    }

    @Override
    public IncomingPaymentBuilder withAmount(BigDecimal amount) {
        pojoBuilder.amount(amount);
        return this;
    }

    @Override
    public IncomingPaymentBuilder withCurrency(Currency currency) {
        pojoBuilder.currency(currency);
        return this;
    }

    @Override
    public IncomingPaymentBuilder withDirection(PaymentDirection direction) {
        pojoBuilder.direction(direction);
        return this;
    }

    @Override
    public IncomingPaymentBuilder withState(PaymentState newState) throws IncomingPaymentIllegalStateTransitionException {
        if (!currentState.canChangeTo(newState)) {
            throw new IncomingPaymentIllegalStateTransitionException(id, currentState, newState);
        }
        pojoBuilder.currentState(newState);
        return this;
    }

    @Override
    public Payment build() throws IncomingPaymentMissingRequiredAttributeException, TraderTeamNotFoundException,
            MerchantNotFoundException, CardNotFoundException, PaymentInvalidAmountException {
        IncomingPaymentPojo payment = pojoBuilder.build();
        validate(payment);
        incomingPaymentRepository.save(payment);
        return payment;
    }

    private void validate(IncomingPaymentPojo pojo) throws PaymentMissingRequiredAttributeException, MerchantNotFoundException,
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
