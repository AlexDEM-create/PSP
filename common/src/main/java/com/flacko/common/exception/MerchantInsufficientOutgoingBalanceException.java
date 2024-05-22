package com.flacko.common.exception;

import com.flacko.common.balance.BalanceLimit;
import com.flacko.common.country.Country;
import com.flacko.common.currency.CurrencyParser;

public class MerchantInsufficientOutgoingBalanceException extends Exception {

    public MerchantInsufficientOutgoingBalanceException(String merchantId, Country country) {
        super(String.format("Merchant %s should have at least %s %s to accept outgoing traffic", merchantId,
                BalanceLimit.getOutgoingTrafficStopLimit(CurrencyParser.parseCurrency(country)),
                CurrencyParser.parseCurrency(country)));
    }

}
