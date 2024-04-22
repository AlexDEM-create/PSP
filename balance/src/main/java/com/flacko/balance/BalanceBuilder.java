package com.flacko.balance;

import com.flacko.balance.exception.BalanceMissingRequiredAttributeException;
import com.flacko.merchant.exception.MerchantNotFoundException;
import com.flacko.trader.team.exception.TraderTeamNotFoundException;

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
