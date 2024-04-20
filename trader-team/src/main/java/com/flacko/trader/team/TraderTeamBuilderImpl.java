package com.flacko.trader.team;

import com.flacko.auth.id.IdGenerator;
import com.flacko.trader.team.exception.TraderTeamMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TraderTeamBuilderImpl implements InitializableTraderTeamBuilder {

    private final Instant now = Instant.now();

    private final TraderTeamRepository traderTeamRepository;

    private TraderTeamPojo.TraderTeamPojoBuilder pojoBuilder;

    @Override
    public TraderTeamBuilder initializeNew() {
        pojoBuilder = TraderTeamPojo.builder()
                .id(new IdGenerator().generateId());
        return this;
    }

    @Override
    public TraderTeamBuilder initializeExisting(TraderTeam existingTraderTeam) {
        pojoBuilder = TraderTeamPojo.builder()
                .primaryKey(existingTraderTeam.getPrimaryKey())
                .id(existingTraderTeam.getId())
                .name(existingTraderTeam.getName())
                .userId(existingTraderTeam.getUserId())
                .leaderId(existingTraderTeam.getLeaderId())
                .traderIncomingFeeRate(existingTraderTeam.getTraderIncomingFeeRate())
                .traderOutgoingFeeRate(existingTraderTeam.getTraderOutgoingFeeRate())
                .leaderIncomingFeeRate(existingTraderTeam.getLeaderIncomingFeeRate())
                .leaderOutgoingFeeRate(existingTraderTeam.getLeaderOutgoingFeeRate())
                .kickedOut(existingTraderTeam.isKickedOut())
                .createdDate(existingTraderTeam.getCreatedDate())
                .updatedDate(now)
                .deletedDate(existingTraderTeam.getDeletedDate().orElse(now));
        return this;
    }

    @Override
    public TraderTeamBuilder withName(String name) {
        pojoBuilder.name(name);
        return this;
    }

    @Override
    public TraderTeamBuilder withUserId(String userId) {
        pojoBuilder.userId(userId);
        return this;
    }

    @Override
    public TraderTeamBuilder withLeaderId(String leaderId) {
        pojoBuilder.leaderId(leaderId);
        return this;
    }

    @Override
    public TraderTeamBuilder withTraderIncomingFeeRate(BigDecimal traderIncomingFeeRate) {
        pojoBuilder.traderIncomingFeeRate(traderIncomingFeeRate);
        return this;
    }

    @Override
    public TraderTeamBuilder withTraderOutgoingFeeRate(BigDecimal traderOutgoingFeeRate) {
        pojoBuilder.traderOutgoingFeeRate(traderOutgoingFeeRate);
        return this;
    }

    @Override
    public TraderTeamBuilder withLeaderIncomingFeeRate(BigDecimal leaderIncomingFeeRate) {
        pojoBuilder.leaderIncomingFeeRate(leaderIncomingFeeRate);
        return this;
    }

    @Override
    public TraderTeamBuilder withLeaderOutgoingFeeRate(BigDecimal leaderOutgoingFeeRate) {
        pojoBuilder.leaderOutgoingFeeRate(leaderOutgoingFeeRate);
        return this;
    }

    @Override
    public TraderTeamBuilder withKickedOut(boolean kickedOut) {
        pojoBuilder.kickedOut(kickedOut);
        return this;
    }

    @Override
    public TraderTeamBuilder withArchived() {
        pojoBuilder.deletedDate(now);
        return this;
    }

    @Override
    public TraderTeam build() throws TraderTeamMissingRequiredAttributeException {
        TraderTeamPojo traderTeam = pojoBuilder.build();
        validate(traderTeam);
        traderTeamRepository.save(traderTeam);
        return traderTeam;
    }

    private void validate(TraderTeamPojo pojo) throws TraderTeamMissingRequiredAttributeException {
        if (pojo.getId() == null || pojo.getId().isEmpty()) {
            throw new TraderTeamMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getName() == null || pojo.getName().isEmpty()) {
            throw new TraderTeamMissingRequiredAttributeException("name", Optional.of(pojo.getId()));
        }
        if (pojo.getUserId() == null || pojo.getUserId().isEmpty()) {
            throw new TraderTeamMissingRequiredAttributeException("userId", Optional.of(pojo.getId()));
        }
        if (pojo.getLeaderId() == null || pojo.getLeaderId().isEmpty()) {
            throw new TraderTeamMissingRequiredAttributeException("leaderId", Optional.of(pojo.getId()));
        }
        if (pojo.getTraderIncomingFeeRate() == null) {
            throw new TraderTeamMissingRequiredAttributeException("traderIncomingFeeRate", Optional.of(pojo.getId()));
        }
        if (pojo.getTraderOutgoingFeeRate() == null) {
            throw new TraderTeamMissingRequiredAttributeException("traderOutgoingFeeRate", Optional.of(pojo.getId()));
        }
        if (pojo.getLeaderIncomingFeeRate() == null) {
            throw new TraderTeamMissingRequiredAttributeException("leaderIncomingFeeRate", Optional.of(pojo.getId()));
        }
        if (pojo.getLeaderOutgoingFeeRate() == null) {
            throw new TraderTeamMissingRequiredAttributeException("leaderOutgoingFeeRate", Optional.of(pojo.getId()));
        }
    }

}
