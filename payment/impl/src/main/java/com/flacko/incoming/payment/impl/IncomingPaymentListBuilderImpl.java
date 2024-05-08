package com.flacko.incoming.payment.impl;

import com.flacko.common.state.PaymentState;
import com.flacko.payment.service.Payment;
import com.flacko.payment.service.PaymentDirection;
import com.flacko.payment.service.PaymentListBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class IncomingPaymentListBuilderImpl implements PaymentListBuilder {

    private final IncomingPaymentRepository incomingPaymentRepository;

    private Optional<String> merchantId = Optional.empty();
    private Optional<String> traderTeamId = Optional.empty();
    private Optional<String> cardId = Optional.empty();
    private Optional<PaymentDirection> direction = Optional.empty();
    private Optional<PaymentState> currentState = Optional.empty();

    @Override
    public PaymentListBuilder withMerchantId(String merchantId) {
        this.merchantId = Optional.ofNullable(merchantId);
        return this;
    }

    @Override
    public PaymentListBuilder withTraderTeamId(String traderTeamId) {
        this.traderTeamId = Optional.ofNullable(traderTeamId);
        return this;
    }

    @Override
    public PaymentListBuilder withCardId(String cardId) {
        this.cardId = Optional.ofNullable(cardId);
        return this;
    }

    @Override
    public PaymentListBuilder withDirection(PaymentDirection direction) {
        this.direction = Optional.ofNullable(direction);
        return this;
    }

    @Override
    public PaymentListBuilder withCurrentState(PaymentState currentState) {
        this.currentState = Optional.ofNullable(currentState);
        return this;
    }

    @Override
    public List<Payment> build() {
        return incomingPaymentRepository.findAll(createSpecification());
    }

    private Specification<Payment> createSpecification() {
        Specification<Payment> spec = Specification.where(null);
        if (merchantId.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("merchant_id"), merchantId.get()));
        }
        if (traderTeamId.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("trader_team_id"), traderTeamId.get()));
        }
        if (cardId.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("card_id"), cardId.get()));
        }
        if (direction.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("direction"), direction.get()));
        }
        if (currentState.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("current_state"), currentState.get()));
        }
        return spec;
    }

}
