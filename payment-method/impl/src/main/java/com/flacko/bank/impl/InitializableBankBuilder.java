package com.flacko.bank.impl;

import com.flacko.bank.service.Bank;
import com.flacko.bank.service.BankBuilder;

public interface InitializableBankBuilder extends BankBuilder {

    BankBuilder initializeNew();

    BankBuilder initializeExisting(Bank existingBank);

}
