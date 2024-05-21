package com.flacko.trader.team.impl;

import com.flacko.balance.service.BalanceService;
import com.flacko.balance.service.BalanceType;
import com.flacko.balance.service.EntityType;
import com.flacko.common.country.Country;
import com.flacko.common.currency.CurrencyParser;
import com.flacko.common.exception.*;
import com.flacko.common.id.IdGenerator;
import com.flacko.common.operation.CrudOperation;
import com.flacko.common.role.UserRole;
import com.flacko.payment.service.outgoing.OutgoingPayment;
import com.flacko.payment.service.outgoing.OutgoingPaymentService;
import com.flacko.trader.team.service.TraderTeam;
import com.flacko.trader.team.service.TraderTeamBuilder;
import com.flacko.trader.team.service.exception.TraderTeamIllegalLeaderException;
import com.flacko.trader.team.service.exception.TraderTeamInvalidFeeRateException;
import com.flacko.trader.team.service.exception.TraderTeamMissingRequiredAttributeException;
import com.flacko.user.service.User;
import com.flacko.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TraderTeamBuilderImpl implements InitializableTraderTeamBuilder {

    private final Instant now = Instant.now();

    private final TraderTeamRepository traderTeamRepository;
    private final UserService userService;
    private final BalanceService balanceService;
    private final OutgoingPaymentService outgoingPaymentService;

    private TraderTeamPojo.TraderTeamPojoBuilder pojoBuilder;
    private CrudOperation crudOperation;

    @Override
    public TraderTeamBuilder initializeNew() {
        crudOperation = CrudOperation.CREATE;
        pojoBuilder = TraderTeamPojo.builder()
                .id(new IdGenerator().generateId())
                .verified(false)
                .incomingOnline(false)
                .outgoingOnline(false)
                .kickedOut(false);
        return this;
    }

    @Override
    public TraderTeamBuilder initializeExisting(TraderTeam existingTraderTeam) {
        crudOperation = CrudOperation.UPDATE;
        pojoBuilder = TraderTeamPojo.builder()
                .primaryKey(existingTraderTeam.getPrimaryKey())
                .id(existingTraderTeam.getId())
                .name(existingTraderTeam.getName())
                .userId(existingTraderTeam.getUserId())
                .country(existingTraderTeam.getCountry())
                .leaderId(existingTraderTeam.getLeaderId())
                .traderIncomingFeeRate(existingTraderTeam.getTraderIncomingFeeRate())
                .traderOutgoingFeeRate(existingTraderTeam.getTraderOutgoingFeeRate())
                .leaderIncomingFeeRate(existingTraderTeam.getLeaderIncomingFeeRate())
                .leaderOutgoingFeeRate(existingTraderTeam.getLeaderOutgoingFeeRate())
                .verified(existingTraderTeam.isVerified())
                .incomingOnline(existingTraderTeam.isIncomingOnline())
                .outgoingOnline(existingTraderTeam.isOutgoingOnline())
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
    public TraderTeamBuilder withVerified() {
        pojoBuilder.verified(true);
        return this;
    }

    @Override
    public TraderTeamBuilder withIncomingOnline(boolean incomingOnline) {
        pojoBuilder.incomingOnline(incomingOnline);
        if (incomingOnline) {
            pojoBuilder.outgoingOnline(false);
        }
        return this;
    }

    @Override
    public TraderTeamBuilder withOutgoingOnline(boolean outgoingOnline) {
        pojoBuilder.outgoingOnline(outgoingOnline);
        if (outgoingOnline) {
            pojoBuilder.incomingOnline(false);
        }
        return this;
    }

    @Override
    public TraderTeamBuilder withKickedOut(boolean kickedOut) {
        pojoBuilder.kickedOut(kickedOut);
        if (kickedOut) {
            pojoBuilder.incomingOnline(false);
            pojoBuilder.outgoingOnline(false);
        }
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
            MerchantNotFoundException, BalanceMissingRequiredAttributeException, TraderTeamNotAllowedOnlineException,
            BalanceInvalidCurrentBalanceException, MerchantInvalidFeeRateException,
            MerchantMissingRequiredAttributeException, OutgoingPaymentIllegalStateTransitionException,
            UnauthorizedAccessException, OutgoingPaymentMissingRequiredAttributeException,
            PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException, OutgoingPaymentNotFoundException,
            NoEligibleTraderTeamsException, MerchantInsufficientOutgoingBalanceException {
        TraderTeamPojo traderTeam = pojoBuilder.build();
        validate(traderTeam);
        traderTeamRepository.save(traderTeam);

        if (traderTeam.isKickedOut()) {
            String login = userService.get(traderTeam.getUserId())
                    .getLogin();
            List<OutgoingPayment> outgoingPayments = outgoingPaymentService.list()
                    .withTraderTeamId(traderTeam.getId())
                    .build();
            for (OutgoingPayment payment : outgoingPayments) {
                outgoingPaymentService.reassignRandomTraderTeam(payment.getId(), login);
            }
        }

        if (crudOperation == CrudOperation.CREATE) {
            balanceService.create()
                    .withEntityId(traderTeam.getId())
                    .withEntityType(EntityType.TRADER_TEAM)
                    .withType(BalanceType.GENERIC)
                    .withCurrency(CurrencyParser.parseCurrency(traderTeam.getCountry()))
                    .build();

            balanceService.create()
                    .withEntityId(traderTeam.getLeaderId())
                    .withEntityType(EntityType.TRADER_TEAM_LEADER)
                    .withType(BalanceType.GENERIC)
                    .withCurrency(CurrencyParser.parseCurrency(traderTeam.getCountry()))
                    .build();
        }

        return traderTeam;
    }

    private void validate(TraderTeamPojo pojo) throws TraderTeamMissingRequiredAttributeException,
            UserNotFoundException, TraderTeamIllegalLeaderException, TraderTeamInvalidFeeRateException,
            TraderTeamNotAllowedOnlineException {
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
        if (pojo.getCountry() == null) {
            throw new TraderTeamMissingRequiredAttributeException("country", Optional.of(pojo.getId()));
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
        if (pojo.isKickedOut() && (pojo.isIncomingOnline() || pojo.isOutgoingOnline())) {
            throw new TraderTeamNotAllowedOnlineException("Trader team is kicked out and cannot be online");
        }
    }

}
