package com.flacko.balance.service;

import com.flacko.common.exception.BalanceMissingRequiredAttributeException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;

import java.math.BigDecimal;

public interface BalanceBuilder {

    BalanceBuilder withEntityId(String entityId);

    BalanceBuilder withEntityType(EntityType entityType);

    BalanceBuilder deposit(BigDecimal amount);

    BalanceBuilder withdraw(BigDecimal amount);

    BalanceBuilder withArchived();

    Balance build() throws BalanceMissingRequiredAttributeException, TraderTeamNotFoundException,
            MerchantNotFoundException;

}
