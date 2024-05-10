package com.flacko.balance.impl;

import com.flacko.balance.service.Balance;
import com.flacko.balance.service.BalanceBuilder;
import com.flacko.balance.service.BalanceType;
import com.flacko.balance.service.EntityType;
import com.flacko.common.currency.Currency;
import com.flacko.common.exception.BalanceMissingRequiredAttributeException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.id.IdGenerator;
import com.flacko.merchant.service.MerchantService;
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
public class BalanceBuilderImpl implements InitializableBalanceBuilder {

    private final Instant now = Instant.now();

    private final BalanceRepository balanceRepository;
    private final TraderTeamService traderTeamService;
    private final MerchantService merchantService;

    private BalancePojo.BalancePojoBuilder pojoBuilder;
    private BigDecimal currentBalance;

    @Override
    public BalanceBuilder initializeNew() {
        currentBalance = BigDecimal.ZERO;
        pojoBuilder = BalancePojo.builder()
                .id(new IdGenerator().generateId())
                .currentBalance(currentBalance);
        return this;
    }

    @Override
    public BalanceBuilder initializeExisting(Balance existingBalance) {
        pojoBuilder = BalancePojo.builder()
                .primaryKey(existingBalance.getPrimaryKey())
                .id(existingBalance.getId())
                .entityId(existingBalance.getEntityId())
                .entityType(existingBalance.getEntityType())
                .currentBalance(existingBalance.getCurrentBalance())
                .createdDate(existingBalance.getCreatedDate())
                .updatedDate(now)
                .deletedDate(existingBalance.getDeletedDate().orElse(null));
        currentBalance = existingBalance.getCurrentBalance();
        return this;
    }

    @Override
    public BalanceBuilder withEntityId(String entityId) {
        pojoBuilder.entityId(entityId);
        return this;
    }

    @Override
    public BalanceBuilder withEntityType(EntityType entityType) {
        pojoBuilder.entityType(entityType);
        return this;
    }

    @Override
    public BalanceBuilder withType(BalanceType type) {
        pojoBuilder.type(type);
        return this;
    }

    @Override
    public BalanceBuilder withCurrency(Currency currency) {
        pojoBuilder.currency(currency);
        return this;
    }

    @Override
    public BalanceBuilder deposit(BigDecimal amount) {
        currentBalance = currentBalance.add(amount);
        pojoBuilder.currentBalance(currentBalance);
        return this;
    }

    @Override
    public BalanceBuilder withdraw(BigDecimal amount) {
        currentBalance = currentBalance.subtract(amount);
        pojoBuilder.currentBalance(currentBalance);
        return this;
    }

    @Override
    public BalanceBuilder withArchived() {
        pojoBuilder.deletedDate(now);
        return this;
    }

    @Override
    public Balance build() throws BalanceMissingRequiredAttributeException, TraderTeamNotFoundException,
            MerchantNotFoundException {
        BalancePojo balance = pojoBuilder.build();
        validate(balance);
        balanceRepository.save(balance);
        return balance;
    }

    private void validate(BalancePojo pojo) throws BalanceMissingRequiredAttributeException, MerchantNotFoundException,
            TraderTeamNotFoundException {
        if (pojo.getId() == null || pojo.getId().isBlank()) {
            throw new BalanceMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getEntityId() == null || pojo.getEntityId().isBlank()) {
            throw new BalanceMissingRequiredAttributeException("entityId", Optional.of(pojo.getId()));
        }
        if (pojo.getEntityType() == null) {
            throw new BalanceMissingRequiredAttributeException("entityType", Optional.of(pojo.getId()));
        }
        if (pojo.getType() == null) {
            throw new BalanceMissingRequiredAttributeException("type", Optional.of(pojo.getId()));
        }
        if (pojo.getEntityType() == EntityType.MERCHANT) {
            merchantService.get(pojo.getEntityId());
        } else if (pojo.getEntityType() == EntityType.TRADER_TEAM) {
            traderTeamService.get(pojo.getEntityId());
        }
        if (pojo.getCurrentBalance() == null) {
            throw new BalanceMissingRequiredAttributeException("currentBalance", Optional.of(pojo.getId()));
        }
        if (pojo.getCurrency() == null) {
            throw new BalanceMissingRequiredAttributeException("currency", Optional.of(pojo.getId()));
        }
    }

}
