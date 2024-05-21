package com.flacko.common.currency;

import com.flacko.common.country.Country;

public class CurrencyParser {

    public static Currency parseCurrency(Country country) {
        return switch (country) {
            case RUSSIA -> Currency.RUB;
            case UZBEKISTAN -> Currency.UZS;
        };
    }

}
