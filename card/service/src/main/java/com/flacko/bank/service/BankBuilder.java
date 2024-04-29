package com.flacko.bank.service;

import com.flacko.bank.service.exception.BankMissingRequiredAttributeException;

public interface BankBuilder {

    BankBuilder withName(String name);

    BankBuilder withCountry(String country);

    BankBuilder withArchived();

    Bank build() throws BankMissingRequiredAttributeException;

}
