package com.flacko.merchant.rest;


import com.flacko.merchant.Merchant;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class MerchantRestMapper {
    MerchantResponse mapModelToResponse(Merchant merchant) {
        MerchantResponse merchantResponse = new MerchantResponse(merchant.getId(),
                merchant.getName(),
                merchant.getCreatedDate().atZone(ZoneId.systemDefault()),
                merchant.getUpdatedDate().atZone(ZoneId.systemDefault()));
        return merchantResponse;
    }
}