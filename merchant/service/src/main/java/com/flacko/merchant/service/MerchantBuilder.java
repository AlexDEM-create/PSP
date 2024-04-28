package com.flacko.merchant.service;

import com.flacko.common.exception.BalanceMissingRequiredAttributeException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.exception.UserNotFoundException;
import com.flacko.merchant.service.exception.MerchantInvalidFeeRateException;
import com.flacko.merchant.service.exception.MerchantMissingRequiredAttributeException;

import java.math.BigDecimal;

public interface MerchantBuilder {

    MerchantBuilder withName(String name);

    MerchantBuilder withUserId(String name);

    MerchantBuilder withIncomingFeeRate(BigDecimal incomingFeeRate);

    MerchantBuilder withOutgoingFeeRate(BigDecimal outgoingFeeRate);

    MerchantBuilder withOutgoingTrafficStopped(boolean outgoingTrafficStopped);

    MerchantBuilder withArchived();

    Merchant build() throws MerchantMissingRequiredAttributeException, UserNotFoundException,
            MerchantInvalidFeeRateException, TraderTeamNotFoundException, MerchantNotFoundException,
            BalanceMissingRequiredAttributeException;

}
