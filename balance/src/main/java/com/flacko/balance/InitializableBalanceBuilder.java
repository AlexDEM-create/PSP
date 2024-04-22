package com.flacko.balance;

public interface InitializableBalanceBuilder extends BalanceBuilder {

    BalanceBuilder initializeNew();

    BalanceBuilder initializeExisting(Balance existingBalance);

}
