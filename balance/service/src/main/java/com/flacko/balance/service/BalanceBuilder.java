package com.flacko.balance.service;

import com.flacko.common.currency.Currency;
import com.flacko.common.exception.*;

import java.math.BigDecimal;

public interface BalanceBuilder {

    BalanceBuilder withEntityId(String entityId);

    BalanceBuilder withEntityType(EntityType entityType);

    BalanceBuilder withType(BalanceType type);

    BalanceBuilder withCurrency(Currency currency);

    BalanceBuilder deposit(BigDecimal amount) throws MerchantNotFoundException;

    BalanceBuilder withdraw(BigDecimal amount);

    BalanceBuilder withArchived();

    Balance build() throws BalanceMissingRequiredAttributeException, TraderTeamNotFoundException,
            MerchantNotFoundException, UserNotFoundException, MerchantInvalidFeeRateException,
            MerchantMissingRequiredAttributeException, BalanceInvalidCurrentBalanceException;

}
