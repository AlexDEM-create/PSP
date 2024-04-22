package com.flacko.balance.rest;

import com.flacko.balance.Balance;
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
