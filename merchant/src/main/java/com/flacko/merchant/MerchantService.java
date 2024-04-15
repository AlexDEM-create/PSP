package com.flacko.merchant;

import com.flacko.merchant.exception.MerchantNotFoundException;

import java.util.List;

public interface MerchantService {
    MerchantBuilder create();

    MerchantBuilder update(String id) throws MerchantNotFoundException;

    MerchantBuilder get(String id) throws MerchantNotFoundException;

    List<MerchantBuilder> list();

}


