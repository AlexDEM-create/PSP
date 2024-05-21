package com.flacko.payment.method.impl;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.payment.method.service.PaymentMethod;
import com.flacko.payment.method.service.PaymentMethodListBuilder;
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
public class PaymentMethodListBuilderImpl implements PaymentMethodListBuilder {

    private final PaymentMethodRepository paymentMethodRepository;

    private Optional<Currency> currency = Optional.empty();
    private Optional<Bank> bank = Optional.empty();
    private Optional<String> traderTeamId = Optional.empty();
    private Optional<String> terminalId = Optional.empty();
    private Optional<Boolean> enabled = Optional.empty();
    private Optional<Boolean> busy = Optional.empty();
    private Optional<Boolean> archived = Optional.empty();

    @Override
    public PaymentMethodListBuilder withCurrency(Currency currency) {
        this.currency = Optional.of(currency);
        return this;
    }

    @Override
    public PaymentMethodListBuilder withBank(Bank bank) {
        this.bank = Optional.of(bank);
        return this;
    }

    @Override
    public PaymentMethodListBuilder withTraderTeamId(String traderTeamId) {
        this.traderTeamId = Optional.of(traderTeamId);
        return this;
    }

    @Override
    public PaymentMethodListBuilder withTerminalId(String terminalId) {
        this.terminalId = Optional.of(terminalId);
        return this;
    }

    @Override
    public PaymentMethodListBuilder withEnabled(Boolean enabled) {
        this.enabled = Optional.of(enabled);
        return this;
    }

    @Override
    public PaymentMethodListBuilder withBusy(Boolean busy) {
        this.busy = Optional.of(busy);
        return this;
    }

    @Override
    public PaymentMethodListBuilder withArchived(Boolean archived) {
        this.archived = Optional.of(archived);
        return this;
    }

    @Override
    public List<PaymentMethod> build() {
        return paymentMethodRepository.findAll(createSpecification());
    }

    private Specification<PaymentMethod> createSpecification() {
        Specification<PaymentMethod> spec = Specification.where(null);
        if (currency.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("currency"), currency.get()));
        }
        if (bank.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("bank"), bank.get()));
        }
        if (traderTeamId.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("traderTeamId"), traderTeamId.get()));
        }
        if (terminalId.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("terminalId"), terminalId.get()));
        }
        if (enabled.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("enabled"), enabled.get()));
        }
        if (busy.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("busy"), busy.get()));
        }
        if (archived.isPresent() && archived.get()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.isNotNull(root.get("deletedDate")));
        }
        return spec;
    }

}
