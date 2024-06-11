package com.flacko.merchant.service;

import com.flacko.common.country.Country;
import com.flacko.common.exception.*;

import java.math.BigDecimal;
import java.net.URL;

public interface MerchantBuilder {

    MerchantBuilder withName(String name);

    MerchantBuilder withUserId(String name);

    MerchantBuilder withCountry(Country country);

    MerchantBuilder withIncomingFeeRate(BigDecimal incomingFeeRate);

    MerchantBuilder withOutgoingFeeRate(BigDecimal outgoingFeeRate);

    MerchantBuilder withWebhook(URL webhook);

    MerchantBuilder withOutgoingTrafficStopped(boolean outgoingTrafficStopped);

    MerchantBuilder withArchived();

    Merchant build() throws MerchantMissingRequiredAttributeException, UserNotFoundException,
            MerchantInvalidFeeRateException, TraderTeamNotFoundException, MerchantNotFoundException,
            BalanceMissingRequiredAttributeException, BalanceInvalidCurrentBalanceException;

}
