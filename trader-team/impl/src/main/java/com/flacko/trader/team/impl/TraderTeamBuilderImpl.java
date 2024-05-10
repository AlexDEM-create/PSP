package com.flacko.trader.team.impl;

import com.flacko.balance.service.BalanceService;
import com.flacko.balance.service.BalanceType;
import com.flacko.balance.service.EntityType;
import com.flacko.common.country.Country;
import com.flacko.common.currency.Currency;
import com.flacko.common.exception.BalanceMissingRequiredAttributeException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.exception.UserNotFoundException;
import com.flacko.common.id.IdGenerator;
import com.flacko.trader.team.service.TraderTeam;
import com.flacko.trader.team.service.TraderTeamBuilder;
import com.flacko.trader.team.service.exception.TraderTeamIllegalLeaderException;
import com.flacko.trader.team.service.exception.TraderTeamInvalidFeeRateException;
import com.flacko.trader.team.service.exception.TraderTeamMissingRequiredAttributeException;
import com.flacko.user.service.User;
import com.flacko.common.role.UserRole;
import com.flacko.user.service.UserService;
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
    private final UserService userService;
    private final BalanceService balanceService;

    private TraderTeamPojo.TraderTeamPojoBuilder pojoBuilder;

    @Override
    public TraderTeamBuilder initializeNew() {
        pojoBuilder = TraderTeamPojo.builder()
                .id(new IdGenerator().generateId())
                .online(false)
                .kickedOut(false);
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
                .online(existingTraderTeam.isOnline())
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
    public TraderTeamBuilder withCountry(Country country) {
        pojoBuilder.country(country);
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
    public TraderTeamBuilder withOnline(boolean online) {
        pojoBuilder.online(online);
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
    public TraderTeam build() throws TraderTeamMissingRequiredAttributeException, UserNotFoundException,
            TraderTeamIllegalLeaderException, TraderTeamInvalidFeeRateException, TraderTeamNotFoundException,
            MerchantNotFoundException, BalanceMissingRequiredAttributeException {
        TraderTeamPojo traderTeam = pojoBuilder.build();
        validate(traderTeam);
        traderTeamRepository.save(traderTeam);

        balanceService.create()
                .withEntityId(traderTeam.getId())
                .withEntityType(EntityType.TRADER_TEAM)
                .withType(BalanceType.GENERIC)
                .withCurrency(parseCurrency(traderTeam.getCountry()))
                .build();

        return traderTeam;
    }

    private void validate(TraderTeamPojo pojo) throws TraderTeamMissingRequiredAttributeException,
            UserNotFoundException, TraderTeamIllegalLeaderException, TraderTeamInvalidFeeRateException {
        if (pojo.getId() == null || pojo.getId().isBlank()) {
            throw new TraderTeamMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getName() == null || pojo.getName().isBlank()) {
            throw new TraderTeamMissingRequiredAttributeException("name", Optional.of(pojo.getId()));
        }
        if (pojo.getUserId() == null || pojo.getUserId().isBlank()) {
            throw new TraderTeamMissingRequiredAttributeException("userId", Optional.of(pojo.getId()));
        } else {
            userService.get(pojo.getUserId());
        }
        if (pojo.getLeaderId() == null || pojo.getLeaderId().isBlank()) {
            throw new TraderTeamMissingRequiredAttributeException("leaderId", Optional.of(pojo.getId()));
        } else {
            User user = userService.get(pojo.getLeaderId());
            if (user.getRole() != UserRole.TRADER_TEAM_LEADER) {
                throw new TraderTeamIllegalLeaderException(pojo.getLeaderId(), pojo.getId());
            }
        }
        if (pojo.getTraderIncomingFeeRate() == null) {
            throw new TraderTeamMissingRequiredAttributeException("traderIncomingFeeRate", Optional.of(pojo.getId()));
        } else if (pojo.getTraderIncomingFeeRate().compareTo(BigDecimal.ZERO) < 0) {
            throw new TraderTeamInvalidFeeRateException("traderIncomingFeeRate", pojo.getId());
        }
        if (pojo.getTraderOutgoingFeeRate() == null) {
            throw new TraderTeamMissingRequiredAttributeException("traderOutgoingFeeRate", Optional.of(pojo.getId()));
        } else if (pojo.getTraderOutgoingFeeRate().compareTo(BigDecimal.ZERO) < 0) {
            throw new TraderTeamInvalidFeeRateException("traderOutgoingFeeRate", pojo.getId());
        }
        if (pojo.getLeaderIncomingFeeRate() == null) {
            throw new TraderTeamMissingRequiredAttributeException("leaderIncomingFeeRate", Optional.of(pojo.getId()));
        } else if (pojo.getLeaderIncomingFeeRate().compareTo(BigDecimal.ZERO) < 0) {
            throw new TraderTeamInvalidFeeRateException("leaderIncomingFeeRate", pojo.getId());
        }
        if (pojo.getLeaderOutgoingFeeRate() == null) {
            throw new TraderTeamMissingRequiredAttributeException("leaderOutgoingFeeRate", Optional.of(pojo.getId()));
        } else if (pojo.getTraderOutgoingFeeRate().compareTo(BigDecimal.ZERO) < 0) {
            throw new TraderTeamInvalidFeeRateException("leaderOutgoingFeeRate", pojo.getId());
        }
    }

    private Currency parseCurrency(Country country) {
        return switch (country) {
            case RUSSIA -> Currency.RUB;
            case UZBEKISTAN -> Currency.UZS;
        };
    }

}
