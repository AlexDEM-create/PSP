package com.flacko.payment.impl;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.common.state.PaymentState;
import com.flacko.payment.impl.incoming.IncomingPaymentRepository;
import com.flacko.payment.impl.outgoing.OutgoingPaymentRepository;
import com.flacko.payment.service.Payment;
import com.flacko.payment.service.PaymentListBuilder;
import com.flacko.payment.service.incoming.IncomingPayment;
import com.flacko.payment.service.outgoing.OutgoingPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class PaymentListBuilderImpl implements PaymentListBuilder {

    private final OutgoingPaymentRepository outgoingPaymentRepository;
    private final IncomingPaymentRepository incomingPaymentRepository;

    private Optional<String> merchantId = Optional.empty();
    private Optional<String> traderTeamId = Optional.empty();
    private Optional<String> paymentMethodId = Optional.empty();
    private Optional<Currency> currency = Optional.empty();
    private Optional<Bank> bank = Optional.empty();
    private Optional<PaymentState> currentState = Optional.empty();
    private Optional<Instant> startDate = Optional.empty();
    private Optional<Instant> endDate = Optional.empty();

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
    public PaymentListBuilder withPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = Optional.ofNullable(paymentMethodId);
        return this;
    }

    @Override
    public PaymentListBuilder withCurrency(Currency currency) {
        this.currency = Optional.of(currency);
        return this;
    }

    @Override
    public PaymentListBuilder withBank(Bank bank) {
        this.bank = Optional.of(bank);
        return this;
    }

    @Override
    public PaymentListBuilder withCurrentState(PaymentState currentState) {
        this.currentState = Optional.ofNullable(currentState);
        return this;
    }

    @Override
    public PaymentListBuilder withStartDate(Instant startDate) {
        this.startDate = Optional.of(startDate);
        return this;
    }

    @Override
    public PaymentListBuilder withEndDate(Instant endDate) {
        this.endDate = Optional.of(endDate);
        return this;
    }

    @Override
    public List<Payment> build() {
        List<Payment> payments = new ArrayList<>();
        payments.addAll(outgoingPaymentRepository.findAll(createSpecificationForOutgoingPayment()));
        payments.addAll(incomingPaymentRepository.findAll(createSpecificationForIncomingPayment()));
        return payments;
    }

    private Specification<OutgoingPayment> createSpecificationForOutgoingPayment() {
        Specification<OutgoingPayment> spec = Specification.where(null);
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
        if (currency.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("currency"), currency.get()));
        }
        if (bank.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("bank"), bank.get()));
        }
        if (currentState.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("currentState"), currentState.get()));
        }
        if (startDate.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), startDate.get()));
        }
        if (endDate.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), endDate.get()));
        }
        return spec;
    }

    private Specification<IncomingPayment> createSpecificationForIncomingPayment() {
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
        if (currency.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("currency"), currency.get()));
        }
        if (bank.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("bank"), bank.get()));
        }
        if (currentState.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("currentState"), currentState.get()));
        }
        if (startDate.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), startDate.get()));
        }
        if (endDate.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), endDate.get()));
        }
        return spec;
    }

}
