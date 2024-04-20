package com.flacko.merchant;

import com.flacko.merchant.exception.MerchantMissingRequiredAttributeException;

import java.math.BigDecimal;

public interface MerchantBuilder {

    MerchantBuilder withName(String name);

    MerchantBuilder withUserId(String name);

    MerchantBuilder withIncomingFeeRate(BigDecimal incomingFeeRate);

    MerchantBuilder withOutgoingFeeRate(BigDecimal outgoingFeeRate);

    MerchantBuilder withOutgoingTrafficStopped(boolean outgoingTrafficStopped);

    MerchantBuilder withArchived();

    Merchant build() throws MerchantMissingRequiredAttributeException;

}
