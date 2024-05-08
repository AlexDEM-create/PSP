package com.flacko.payment.method.impl;

import com.flacko.common.currency.Currency;
import com.flacko.payment.method.service.PaymentMethod;
import com.flacko.payment.method.service.PaymentMethodListBuilder;
import com.flacko.payment.method.service.PaymentMethodType;
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

    private Optional<PaymentMethodType> type = Optional.empty();
    private Optional<Currency> currency = Optional.empty();
    private Optional<String> bankId = Optional.empty();
    private Optional<String> traderTeamId = Optional.empty();
    private Optional<String> terminalId = Optional.empty();
    private Optional<Boolean> busy = Optional.empty();

    @Override
    public PaymentMethodListBuilder withType(PaymentMethodType type) {
        this.type = Optional.of(type);
        return this;
    }

    @Override
    public PaymentMethodListBuilder withCurrency(Currency currency) {
        this.currency = Optional.of(currency);
        return this;
    }

    @Override
    public PaymentMethodListBuilder withBankId(String bankId) {
        this.bankId = Optional.of(bankId);
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
    public PaymentMethodListBuilder withBusy(boolean busy) {
        this.busy = Optional.of(busy);
        return this;
    }

    @Override
    public List<PaymentMethod> build() {
        return paymentMethodRepository.findAll(createSpecification());
    }

    private Specification<PaymentMethod> createSpecification() {
        Specification<PaymentMethod> spec = Specification.where(null);
        if (type.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("type"), type.get()));
        }
        if (currency.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("currency"), currency.get()));
        }
        if (bankId.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("bank_id"), bankId.get()));
        }
        if (traderTeamId.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("trade_team_id"), traderTeamId.get()));
        }
        if (terminalId.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("terminal_id"), terminalId.get()));
        }
        if (busy.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("busy"), busy.get()));
        }
        return spec;
    }

}
