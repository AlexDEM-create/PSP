package com.flacko.trader.team.impl;

import com.flacko.trader.team.service.TraderTeam;
import com.flacko.trader.team.service.TraderTeamListBuilder;
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
public class TraderTeamListBuilderImpl implements TraderTeamListBuilder {

    private final TraderTeamRepository traderTeamRepository;

    private Optional<Boolean> online = Optional.empty();
    private Optional<Boolean> kickedOut = Optional.empty();
    private Optional<String> leaderId = Optional.empty();

    @Override
    public TraderTeamListBuilder withOnline(Boolean online) {
        this.online = Optional.of(online);
        return this;
    }

    @Override
    public TraderTeamListBuilder withKickedOut(Boolean kickedOut) {
        this.kickedOut = Optional.of(kickedOut);
        return this;
    }

    @Override
    public TraderTeamListBuilder withLeaderId(String leaderId) {
        this.leaderId = Optional.ofNullable(leaderId);
        return this;
    }

    @Override
    public List<TraderTeam> build() {
        return traderTeamRepository.findAll(createSpecification());
    }

    private Specification<TraderTeam> createSpecification() {
        Specification<TraderTeam> spec = Specification.where(null);
        if (online.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("online"), online.get()));
        }
        if (kickedOut.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("kicked_out"), kickedOut.get()));
        }
        if (leaderId.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("leader_id"), leaderId.get()));
        }
        return spec;
    }

}
