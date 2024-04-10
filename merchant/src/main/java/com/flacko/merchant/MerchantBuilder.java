package com.flacko.merchant;

import com.flacko.merchant.exception.MerchantMissingRequiredAttributeException;

public interface MerchantBuilder {
    MerchantBuilder withId(String id);
    MerchantBuilder withName(String name);
    Merchant build() throws MerchantMissingRequiredAttributeException;
}