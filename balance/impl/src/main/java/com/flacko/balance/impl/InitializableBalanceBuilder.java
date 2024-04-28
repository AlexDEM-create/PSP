package com.flacko.balance.impl;

import com.flacko.balance.service.Balance;
import com.flacko.balance.service.BalanceBuilder;

public interface InitializableBalanceBuilder extends BalanceBuilder {

    BalanceBuilder initializeNew();

    BalanceBuilder initializeExisting(Balance existingBalance);

}
