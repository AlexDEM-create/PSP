package com.flacko.trader.team.impl;

import com.flacko.common.country.Country;
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

    private Optional<Boolean> verified = Optional.empty();
    private Optional<Boolean> incomingOnline = Optional.empty();
    private Optional<Boolean> outgoingOnline = Optional.empty();
    private Optional<Boolean> kickedOut = Optional.empty();
    private Optional<String> leaderId = Optional.empty();
    private Optional<Country> country = Optional.empty();
    private Optional<Boolean> archived = Optional.empty();

    @Override
    public TraderTeamListBuilder withVerified(Boolean verified) {
        this.verified = Optional.of(verified);
        return this;
    }

    @Override
    public TraderTeamListBuilder withIncomingOnline(Boolean incomingOnline) {
        this.incomingOnline = Optional.of(incomingOnline);
        return this;
    }

    @Override
    public TraderTeamListBuilder withOutgoingOnline(Boolean outgoingOnline) {
        this.outgoingOnline = Optional.of(outgoingOnline);
        return this;
    }

    @Override
    public TraderTeamListBuilder withKickedOut(Boolean kickedOut) {
        this.kickedOut = Optional.of(kickedOut);
        return this;
    }

    @Override
    public TraderTeamListBuilder withLeaderId(String leaderId) {
        this.leaderId = Optional.of(leaderId);
        return this;
    }

    @Override
    public TraderTeamListBuilder withCountry(Country country) {
        this.country = Optional.of(country);
        return this;
    }

    @Override
    public TraderTeamListBuilder withArchived(Boolean archived) {
        this.archived = Optional.of(archived);
        return this;
    }

    @Override
    public List<TraderTeam> build() {
        return traderTeamRepository.findAll(createSpecification());
    }

    private Specification<TraderTeam> createSpecification() {
        Specification<TraderTeam> spec = Specification.where(null);
        if (verified.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("verified"), verified.get()));
        }
        if (incomingOnline.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("incomingOnline"), incomingOnline.get()));
        }
        if (outgoingOnline.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("outgoingOnline"), outgoingOnline.get()));
        }
        if (kickedOut.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("kickedOut"), kickedOut.get()));
        }
        if (leaderId.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("leaderId"), leaderId.get()));
        }
        if (country.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("country"), country.get()));
        }
        if (archived.isPresent() && archived.get()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.isNotNull(root.get("deletedDate")));
        }
        return spec;
    }

}
