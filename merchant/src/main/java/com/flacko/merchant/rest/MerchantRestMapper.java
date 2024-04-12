package com.flacko.merchant.rest;


import com.flacko.merchant.Merchant;
import org.springframework.stereotype.Component;

@Component
public class MerchantRestMapper {
    MerchantResponse mapModelToResponse(Merchant merchant) {
        return new MerchantResponse(merchant.getId(),
                merchant.getName(),
                merchant.getCreatedDate(),
                merchant.getUpdatedDate());
    }
}