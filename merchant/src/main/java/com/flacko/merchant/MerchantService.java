package com.flacko.merchant;

import com.flacko.merchant.exception.MerchantNotFoundException;

import java.util.List;

public interface MerchantService {
    Merchant create(Merchant merchant);
    Merchant update(String id, Merchant merchant);
    Merchant get(String id) throws MerchantNotFoundException;
    List<Merchant> list();

    void delete(String id);
}
//    void delete(int merchantId);


