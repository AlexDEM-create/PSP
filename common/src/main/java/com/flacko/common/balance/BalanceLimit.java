package com.flacko.common.balance;

import com.flacko.common.currency.Currency;

import java.math.BigDecimal;

public class BalanceLimit {

    public static BigDecimal getOutgoingTrafficStopLimit(Currency currency) {
        return switch (currency) {
            case RUB -> BigDecimal.valueOf(50000);
            case USDT -> BigDecimal.valueOf(500);
            case UZS -> BigDecimal.valueOf(6500000);
        };
    }

}
