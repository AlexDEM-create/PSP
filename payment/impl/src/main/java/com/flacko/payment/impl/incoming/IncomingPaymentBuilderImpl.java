package com.flacko.payment.impl.incoming;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.OutgoingPaymentMissingRequiredAttributeException;
import com.flacko.common.exception.PaymentMethodNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.id.IdGenerator;
import com.flacko.common.state.PaymentState;
import com.flacko.merchant.service.MerchantService;
import com.flacko.payment.method.service.PaymentMethodService;
import com.flacko.payment.service.incoming.IncomingPayment;
import com.flacko.payment.service.incoming.IncomingPaymentBuilder;
import com.flacko.payment.service.incoming.exception.IncomingPaymentIllegalStateTransitionException;
import com.flacko.payment.service.incoming.exception.IncomingPaymentInvalidAmountException;
import com.flacko.payment.service.incoming.exception.IncomingPaymentMissingRequiredAttributeException;
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
    private final PaymentMethodService paymentMethodService;

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
    public IncomingPaymentBuilder initializeExisting(IncomingPayment existingIncomingPayment) {
        pojoBuilder = IncomingPaymentPojo.builder()
                .primaryKey(existingIncomingPayment.getPrimaryKey())
                .id(existingIncomingPayment.getId())
                .merchantId(existingIncomingPayment.getMerchantId())
                .traderTeamId(existingIncomingPayment.getTraderTeamId())
                .paymentMethodId(existingIncomingPayment.getPaymentMethodId())
                .amount(existingIncomingPayment.getAmount())
                .currency(existingIncomingPayment.getCurrency())
                .bank(existingIncomingPayment.getBank())
                .currentState(existingIncomingPayment.getCurrentState())
                .createdDate(existingIncomingPayment.getCreatedDate())
                .updatedDate(Instant.now());
        id = existingIncomingPayment.getId();
        currentState = existingIncomingPayment.getCurrentState();
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
    public IncomingPaymentBuilder withPaymentMethodId(String paymentMethodId) {
        pojoBuilder.paymentMethodId(paymentMethodId);
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
    public IncomingPaymentBuilder withBank(Bank bank) {
        pojoBuilder.bank(bank);
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
    public IncomingPayment build() throws IncomingPaymentMissingRequiredAttributeException, TraderTeamNotFoundException,
            MerchantNotFoundException, PaymentMethodNotFoundException, IncomingPaymentInvalidAmountException {
        IncomingPaymentPojo payment = pojoBuilder.build();
        validate(payment);
        incomingPaymentRepository.save(payment);
        return payment;
    }

    private void validate(IncomingPaymentPojo pojo) throws IncomingPaymentMissingRequiredAttributeException,
            MerchantNotFoundException, TraderTeamNotFoundException, PaymentMethodNotFoundException,
            IncomingPaymentInvalidAmountException {
        if (pojo.getId() == null || pojo.getId().isBlank()) {
            throw new IncomingPaymentMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getMerchantId() == null || pojo.getMerchantId().isBlank()) {
            throw new IncomingPaymentMissingRequiredAttributeException("merchantId", Optional.of(pojo.getId()));
        } else {
            merchantService.get(pojo.getMerchantId());
        }
        if (pojo.getTraderTeamId() == null || pojo.getTraderTeamId().isBlank()) {
            throw new IncomingPaymentMissingRequiredAttributeException("traderTeamId", Optional.of(pojo.getId()));
        } else {
            traderTeamService.get(pojo.getTraderTeamId());
        }
        if (pojo.getPaymentMethodId() == null || pojo.getPaymentMethodId().isBlank()) {
            throw new IncomingPaymentMissingRequiredAttributeException("paymentMethodId", Optional.of(pojo.getId()));
        } else {
            paymentMethodService.get(pojo.getPaymentMethodId());
        }
        if (pojo.getAmount() == null) {
            throw new IncomingPaymentMissingRequiredAttributeException("amount", Optional.of(pojo.getId()));
        } else if (pojo.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IncomingPaymentInvalidAmountException(pojo.getId());
        }
        if (pojo.getCurrency() == null) {
            throw new IncomingPaymentMissingRequiredAttributeException("currency", Optional.of(pojo.getId()));
        }
        if (pojo.getBank() == null) {
            throw new IncomingPaymentMissingRequiredAttributeException("bank", Optional.of(pojo.getId()));
        }
        if (pojo.getCurrentState() == null) {
            throw new IncomingPaymentMissingRequiredAttributeException("currentState", Optional.of(pojo.getId()));
        }
    }

}
