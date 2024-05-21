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
    private Optional<Boolean> enabled = Optional.empty();
    private Optional<Boolean> online = Optional.empty();
    private Optional<Boolean> archived = Optional.empty();

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
    public TerminalListBuilder withEnabled(Boolean enabled) {
        this.enabled = Optional.of(enabled);
        return this;
    }

    @Override
    public TerminalListBuilder withOnline(Boolean online) {
        this.online = Optional.of(online);
        return this;
    }

    @Override
    public TerminalListBuilder withArchived(Boolean archived) {
        this.archived = Optional.of(archived);
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
                    criteriaBuilder.equal(root.get("traderTeamId"), traderTeamId.get()));
        }
        if (verified.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("verified"), verified.get()));
        }
        if (enabled.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("enabled"), enabled.get()));
        }
        if (online.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("online"), online.get()));
        }
        if (archived.isPresent() && archived.get()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.isNotNull(root.get("deletedDate")));
        }
        return spec;
    }

}
