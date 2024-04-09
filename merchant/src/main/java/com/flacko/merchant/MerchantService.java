package com.flacko.merchant;

import java.util.List;

public interface MerchantService {
    Merchant create(Merchant merchant);
    Merchant update(String id, Merchant merchant);
    Merchant get(String id);
    List<Merchant> list();
    void delete(String id);
}
