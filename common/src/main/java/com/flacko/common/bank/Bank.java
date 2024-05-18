package com.flacko.common.bank;

import com.flacko.common.country.Country;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Bank {

    SBER(Country.RUSSIA),
    RAIFFEISEN(Country.RUSSIA);

    private final Country country;

}
