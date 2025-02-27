package com.flacko.merchant.webapp.rest;


import com.flacko.merchant.service.Merchant;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class MerchantRestMapper {

    MerchantResponse mapModelToResponse(Merchant merchant) {
        return new MerchantResponse(merchant.getId(),
                merchant.getName(),
                merchant.getUserId(),
                merchant.getCountry(),
                merchant.getIncomingFeeRate().movePointRight(2),
                merchant.getOutgoingFeeRate().movePointRight(2),
                merchant.getWebhook(),
                merchant.isOutgoingTrafficStopped(),
                merchant.getCreatedDate().atZone(ZoneId.systemDefault()),
                merchant.getUpdatedDate().atZone(ZoneId.systemDefault()));
    }

}
