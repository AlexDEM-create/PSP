package com.flacko.merchant;

import com.flacko.auth.security.user.exception.UserNotFoundException;
import com.flacko.balance.exception.BalanceMissingRequiredAttributeException;
import com.flacko.merchant.exception.MerchantInvalidFeeRateException;
import com.flacko.merchant.exception.MerchantMissingRequiredAttributeException;
import com.flacko.merchant.exception.MerchantNotFoundException;
import com.flacko.trader.team.exception.TraderTeamNotFoundException;

import java.math.BigDecimal;

public interface MerchantBuilder {

    MerchantBuilder withName(String name);

    MerchantBuilder withUserId(String name);

    MerchantBuilder withIncomingFeeRate(BigDecimal incomingFeeRate);

    MerchantBuilder withOutgoingFeeRate(BigDecimal outgoingFeeRate);

    MerchantBuilder withOutgoingTrafficStopped(boolean outgoingTrafficStopped);

    MerchantBuilder withArchived();

    Merchant build() throws MerchantMissingRequiredAttributeException, UserNotFoundException,
            MerchantInvalidFeeRateException, TraderTeamNotFoundException, MerchantNotFoundException, BalanceMissingRequiredAttributeException;

}
