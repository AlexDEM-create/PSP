package com.flacko.terminal.impl;

import com.flacko.terminal.service.Terminal;
import com.flacko.terminal.service.TerminalListBuilder;
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
public class TerminalListBuilderImpl implements TerminalListBuilder {

    private final TerminalRepository terminalRepository;

    private Optional<String> traderTeamId = Optional.empty();
    private Optional<Boolean> verified = Optional.empty();
    private Optional<Boolean> active = Optional.empty();

    @Override
    public TerminalListBuilder withTraderTeamId(String traderTeamId) {
        this.traderTeamId = Optional.of(traderTeamId);
        return this;
    }

    @Override
    public TerminalListBuilder withVerified(Boolean verified) {
        this.verified = Optional.of(verified);
        return this;
    }

    @Override
    public TerminalListBuilder withActive(Boolean active) {
        this.active = Optional.of(active);
        return this;
    }

    @Override
    public List<Terminal> build() {
        return terminalRepository.findAll(createSpecification());
    }

    private Specification<Terminal> createSpecification() {
        Specification<Terminal> spec = Specification.where(null);
        if (traderTeamId.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("trader_team_id"), traderTeamId.get()));
        }
        if (verified.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("verified"), verified.get()));
        }
        if (active.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("active"), active.get()));
        }
        return spec;
    }

}
