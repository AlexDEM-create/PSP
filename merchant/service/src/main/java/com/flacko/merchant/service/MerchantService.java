package com.flacko.merchant.service;

import com.flacko.common.exception.MerchantNotFoundException;

public interface MerchantService {

    MerchantBuilder create();

    MerchantBuilder update(String id) throws MerchantNotFoundException;

    MerchantListBuilder list();

    Merchant get(String id) throws MerchantNotFoundException;

    Merchant getByUserId(String userId) throws MerchantNotFoundException;

}


