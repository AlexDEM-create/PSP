package impl;


import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.id.IdGenerator;
import com.flacko.merchant.service.MerchantService;
import com.flacko.trader.team.service.TraderTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import service.EntityType;
import service.Stats;
import service.StatsBuilder;
import service.exception.StatsMissingRequiredAttributeException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class StatsBuilderImpl implements InitializableStatsBuilder {

    private final Instant now = Instant.now();

    private final StatsRepository statsRepository;
    private final TraderTeamService traderTeamService;
    private final MerchantService merchantService;

    private StatsPojo pojoBuilder;
    private BigDecimal todayOutgoingTotal;
    private BigDecimal todayIncomingTotal;
    private BigDecimal allTimeOutgoingTotal;
    private BigDecimal allTimeIncomingTotal;

    @Override
    public StatsBuilder initializeNew() {
        todayOutgoingTotal = BigDecimal.ZERO;
        todayIncomingTotal = BigDecimal.ZERO;
        allTimeOutgoingTotal = BigDecimal.ZERO;
        allTimeIncomingTotal = BigDecimal.ZERO;
        pojoBuilder = StatsPojo.builder()
                .id(new IdGenerator().generateId())
                .todayOutgoingTotal(todayOutgoingTotal)
                .todayIncomingTotal(todayIncomingTotal)
                .allTimeOutgoingTotal(allTimeOutgoingTotal)
                .allTimeIncomingTotal(allTimeIncomingTotal).build();
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
                .allTimeIncomingTotal(existingStats.getAllTimeIncomingTotal()).build();
        return this;
    }
    @Override
    public StatsBuilder withEntityId(String entityId) {
        pojoBuilder.setEntityId(entityId);
        return this;
    }

    @Override
    public StatsBuilder withEntityType(EntityType entityType) {
        pojoBuilder.setEntityType(entityType);
        return this;
    }
    @Override
    public StatsBuilder withTodayOutgoingTotal(BigDecimal amount) {
        pojoBuilder.setTodayOutgoingTotal(amount);
        return this;
    }

    @Override
    public StatsBuilder withTodayIncomingTotal(BigDecimal amount) {
        pojoBuilder.setTodayIncomingTotal(amount);
        return this;
    }

    @Override
    public StatsBuilder withAllTimeOutgoingTotal(BigDecimal amount) {
        pojoBuilder.setAllTimeOutgoingTotal(amount);
        return this;
    }

    @Override
    public StatsBuilder withAllTimeIncomingTotal(BigDecimal amount) {
        pojoBuilder.setAllTimeIncomingTotal(amount);
        return this;
    }

    @Override
    public Stats build() throws StatsMissingRequiredAttributeException, TraderTeamNotFoundException,
            MerchantNotFoundException {
        StatsPojo stats = pojoBuilder.toBuilder().build();
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
        if (pojo.getTodayOutgoingTotal() == null || pojo.getTodayIncomingTotal() == null ||
                pojo.getAllTimeOutgoingTotal() == null || pojo.getAllTimeIncomingTotal() == null) {
            throw new StatsMissingRequiredAttributeException("payment totals", Optional.of(pojo.getId()));
        }
    }
}

