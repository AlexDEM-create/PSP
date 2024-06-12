package com.flacko.stats.impl;

import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.id.IdGenerator;
import com.flacko.merchant.service.MerchantService;
import com.flacko.stats.service.EntityType;
import com.flacko.stats.service.Stats;
import com.flacko.stats.service.StatsBuilder;
import com.flacko.stats.service.exception.StatsMissingRequiredAttributeException;
import com.flacko.trader.team.service.TraderTeamService;
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
public class StatsBuilderImpl implements InitializableStatsBuilder {

    private final StatsRepository statsRepository;
    private final TraderTeamService traderTeamService;
    private final MerchantService merchantService;

    private StatsPojo.StatsPojoBuilder pojoBuilder;

    @Override
    public StatsBuilder initializeNew() {
        pojoBuilder = StatsPojo.builder()
                .id(new IdGenerator().generateId())
                .todayOutgoingTotal(BigDecimal.ZERO)
                .todayIncomingTotal(BigDecimal.ZERO)
                .allTimeOutgoingTotal(BigDecimal.ZERO)
                .allTimeIncomingTotal(BigDecimal.ZERO);
        return this;
    }

    @Override
    public StatsBuilder initializeExisting(Stats existingStats) {
        pojoBuilder = StatsPojo.builder()
                .primaryKey(existingStats.getPrimaryKey())
                .id(existingStats.getId())
                .entityId(existingStats.getEntityId())
                .entityType(existingStats.getEntityType())
                .todayOutgoingTotal(existingStats.getTodayOutgoingTotal())
                .todayIncomingTotal(existingStats.getTodayIncomingTotal())
                .allTimeOutgoingTotal(existingStats.getAllTimeOutgoingTotal())
                .allTimeIncomingTotal(existingStats.getAllTimeIncomingTotal())
                .createdDate(existingStats.getCreatedDate())
                .updatedDate(Instant.now());
        return this;
    }

    @Override
    public StatsBuilder withEntityId(String entityId) {
        pojoBuilder.entityId(entityId);
        return this;
    }

    @Override
    public StatsBuilder withEntityType(EntityType entityType) {
        pojoBuilder.entityType(entityType);
        return this;
    }

    @Override
    public StatsBuilder withTodayOutgoingTotal(BigDecimal todayOutgoingTotal) {
        pojoBuilder.todayOutgoingTotal(todayOutgoingTotal);
        return this;
    }

    @Override
    public StatsBuilder withTodayIncomingTotal(BigDecimal todayIncomingTotal) {
        pojoBuilder.todayIncomingTotal(todayIncomingTotal);
        return this;
    }

    @Override
    public StatsBuilder withAllTimeOutgoingTotal(BigDecimal allTimeOutgoingTotal) {
        pojoBuilder.allTimeOutgoingTotal(allTimeOutgoingTotal);
        return this;
    }

    @Override
    public StatsBuilder withAllTimeIncomingTotal(BigDecimal allTimeIncomingTotal) {
        pojoBuilder.allTimeIncomingTotal(allTimeIncomingTotal);
        return this;
    }

    @Override
    public Stats build() throws StatsMissingRequiredAttributeException, TraderTeamNotFoundException,
            MerchantNotFoundException {
        StatsPojo stats = pojoBuilder.build();
        validate(stats);
        statsRepository.save(stats);
        return stats;
    }

    private void validate(StatsPojo pojo) throws StatsMissingRequiredAttributeException, MerchantNotFoundException,
            TraderTeamNotFoundException {
        if (pojo.getId() == null || pojo.getId().isBlank()) {
            throw new StatsMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getEntityId() == null || pojo.getEntityId().isBlank()) {
            throw new StatsMissingRequiredAttributeException("entityId", Optional.of(pojo.getId()));
        }
        if (pojo.getEntityType() == null) {
            throw new StatsMissingRequiredAttributeException("entityType", Optional.of(pojo.getId()));
        }
        if (pojo.getEntityType() == EntityType.MERCHANT) {
            merchantService.get(pojo.getEntityId());
        } else if (pojo.getEntityType() == EntityType.TRADER_TEAM) {
            traderTeamService.get(pojo.getEntityId());
        }
        if (pojo.getTodayOutgoingTotal() == null) {
            throw new StatsMissingRequiredAttributeException("todayOutgoingTotal", Optional.of(pojo.getId()));
        }
        if (pojo.getTodayIncomingTotal() == null) {
            throw new StatsMissingRequiredAttributeException("todayIncomingTotal", Optional.of(pojo.getId()));
        }
        if (pojo.getAllTimeOutgoingTotal() == null) {
            throw new StatsMissingRequiredAttributeException("allTimeOutgoingTotal", Optional.of(pojo.getId()));
        }
        if (pojo.getAllTimeIncomingTotal() == null) {
            throw new StatsMissingRequiredAttributeException("allTimeIncomingTotal", Optional.of(pojo.getId()));
        }
    }

}
