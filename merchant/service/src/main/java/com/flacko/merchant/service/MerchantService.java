package com.flacko.merchant.service;

import com.flacko.common.exception.MerchantNotFoundException;

import java.util.List;

public interface MerchantService {

    MerchantBuilder create();

    MerchantBuilder update(String id) throws MerchantNotFoundException;

    List<Merchant> list();

    Merchant get(String id) throws MerchantNotFoundException;

}


