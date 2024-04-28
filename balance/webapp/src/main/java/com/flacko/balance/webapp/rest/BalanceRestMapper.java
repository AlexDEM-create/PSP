package com.flacko.balance.webapp.rest;

import com.flacko.balance.service.Balance;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class BalanceRestMapper {

    BalanceResponse mapModelToResponse(Balance balance) {
        return new BalanceResponse(balance.getId(),
                balance.getEntityId(),
                balance.getEntityType(),
                balance.getCurrentBalance(),
                balance.getCreatedDate().atZone(ZoneId.systemDefault()),
                balance.getUpdatedDate().atZone(ZoneId.systemDefault()));
    }

}
