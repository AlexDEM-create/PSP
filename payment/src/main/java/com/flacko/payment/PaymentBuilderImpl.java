package com.flacko.payment;

import com.flacko.auth.id.IdGenerator;
import com.flacko.payment.exception.PaymentMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class PaymentBuilderImpl implements InitializablePaymentBuilder {

    private PaymentPojo.PaymentPojoBuilder pojoBuilder;

    @Override
    public PaymentBuilder initializeNew() {
        pojoBuilder = PaymentPojo.builder()
                .id(new IdGenerator().generateId())
                .currentState(PaymentState.INITIATED);
        return this;
    }

    @Override
    public PaymentBuilder initializeExisting(Payment existingPayment) {
        // need to solve the problem with primary key
        pojoBuilder = PaymentPojo.builder()
                .id(existingPayment.getId())
                .merchantId(existingPayment.getMerchantId())
                .traderId(existingPayment.getTraderId())
                .cardId(existingPayment.getCardId())
                .currentState(existingPayment.getCurrentState())
                .updatedDate(Instant.now());
        return this;
    }

    @Override
    public PaymentBuilder withMerchantId(String merchantId) {
        pojoBuilder.merchantId(merchantId);
        return this;
    }

    //todo
    @Override
    public PaymentBuilder withTraderId(String traderId) {
        pojoBuilder.traderId(traderId);
        return this;
    }

    @Override
    public PaymentBuilder withCardId(String cardId) {
        pojoBuilder.cardId(cardId);
        return this;
    }

    @Override
    public PaymentBuilder withCurrentState(PaymentState currentState) {
        pojoBuilder.currentState(currentState);
        return this;
    }

    @Override
    public Payment build() throws PaymentMissingRequiredAttributeException {
        PaymentPojo payment = pojoBuilder.build();
        validate(payment);
        return payment;
    }

    private void validate(PaymentPojo pojo) throws PaymentMissingRequiredAttributeException {
        if (pojo.getId() == null || pojo.getId().isEmpty()) {
            throw new PaymentMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getMerchantId() == null || pojo.getMerchantId().isEmpty()) {
            throw new PaymentMissingRequiredAttributeException("merchantId", Optional.of(pojo.getId()));
        }
        if (pojo.getTraderId() == null || pojo.getTraderId().isEmpty()) {
            throw new PaymentMissingRequiredAttributeException("traderId", Optional.of(pojo.getId()));
        }
        if (pojo.getCardId() == null || pojo.getCardId().isEmpty()) {
            throw new PaymentMissingRequiredAttributeException("cardId", Optional.of(pojo.getId()));
        }
        if (pojo.getCurrentState() == null) {
            throw new PaymentMissingRequiredAttributeException("currentState", Optional.of(pojo.getId()));
        }
    }

}

