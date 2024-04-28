package com.flacko.merchant.impl;

import com.flacko.merchant.service.Merchant;
import com.flacko.merchant.service.MerchantBuilder;

public interface InitializableMerchantBuilder extends MerchantBuilder {

    MerchantBuilder initializeNew();

    MerchantBuilder initializeExisting(Merchant existingMerchant);
}
