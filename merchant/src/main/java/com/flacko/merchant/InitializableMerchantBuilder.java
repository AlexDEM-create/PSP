package com.flacko.merchant;

public interface InitializableMerchantBuilder extends MerchantBuilder {
    MerchantBuilder initializeNew();
    MerchantBuilder initializeExisting(Merchant existingMerchant);
}
