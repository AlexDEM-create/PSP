package com.flacko.payment.impl.incoming;

import com.flacko.common.state.PaymentState;
import com.flacko.payment.service.incoming.IncomingPayment;
import com.flacko.payment.service.incoming.IncomingPaymentListBuilder;
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
public class IncomingPaymentListBuilderImpl implements IncomingPaymentListBuilder {

    private final IncomingPaymentRepository incomingPaymentRepository;

    private Optional<String> merchantId = Optional.empty();
    private Optional<String> traderTeamId = Optional.empty();
    private Optional<String> paymentMethodId = Optional.empty();
    private Optional<PaymentState> currentState = Optional.empty();

    @Override
    public IncomingPaymentListBuilder withMerchantId(String merchantId) {
        this.merchantId = Optional.ofNullable(merchantId);
        return this;
    }

    @Override
    public IncomingPaymentListBuilder withTraderTeamId(String traderTeamId) {
        this.traderTeamId = Optional.ofNullable(traderTeamId);
        return this;
    }

    public IncomingPaymentListBuilder withPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = Optional.ofNullable(paymentMethodId);
        return this;
    }

    @Override
    public IncomingPaymentListBuilder withCurrentState(PaymentState currentState) {
        this.currentState = Optional.ofNullable(currentState);
        return this;
    }

    @Override
    public List<IncomingPayment> build() {
        return incomingPaymentRepository.findAll(createSpecification());
    }

    private Specification<IncomingPayment> createSpecification() {
        Specification<IncomingPayment> spec = Specification.where(null);
        if (merchantId.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("merchantId"), merchantId.get()));
        }
        if (traderTeamId.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("traderTeamId"), traderTeamId.get()));
        }
        if (paymentMethodId.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("paymentMethodId"), paymentMethodId.get()));
        }
        if (currentState.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("currentState"), currentState.get()));
        }
        return spec;
    }

}
