package com.flacko.card.impl;

import com.flacko.card.service.Card;
import com.flacko.card.service.CardListBuilder;
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
public class CardListBuilderImpl implements CardListBuilder {

    private final CardRepository cardRepository;

    private Optional<String> bankId = Optional.empty();
    private Optional<String> traderTeamId = Optional.empty();
    private Optional<String> terminalId = Optional.empty();
    private Optional<Boolean> busy = Optional.empty();

    @Override
    public CardListBuilder withBankId(String bankId) {
        this.bankId = Optional.of(bankId);
        return this;
    }

    @Override
    public CardListBuilder withTraderTeamId(String traderTeamId) {
        this.traderTeamId = Optional.of(traderTeamId);
        return this;
    }

    @Override
    public CardListBuilder withTerminalId(String terminalId) {
        this.terminalId = Optional.of(terminalId);
        return this;
    }

    @Override
    public CardListBuilder withBusy(boolean busy) {
        this.busy = Optional.of(busy);
        return this;
    }

    @Override
    public List<Card> build() {
        return cardRepository.findAll(createSpecification());
    }

    private Specification<Card> createSpecification() {
        Specification<Card> spec = Specification.where(null);
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
