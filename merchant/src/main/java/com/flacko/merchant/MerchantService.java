package com.flacko.merchant;

import java.util.List;

public interface MerchantService {
    Merchant create(Merchant merchant);
    Merchant update(int merchantId, Merchant merchant);
    Merchant get(int merchantId);
    List<Merchant> list();
    void delete(int merchantId);
}
