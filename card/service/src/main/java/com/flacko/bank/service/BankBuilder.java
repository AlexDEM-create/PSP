package com.flacko.bank.service;

import com.flacko.bank.service.exception.BankMissingRequiredAttributeException;
import com.flacko.common.country.Country;

public interface BankBuilder {

    BankBuilder withName(String name);

    BankBuilder withCountry(Country country);

    BankBuilder withArchived();

    Bank build() throws BankMissingRequiredAttributeException;

}
