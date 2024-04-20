package com.flacko.trader.rest;

import com.flacko.trader.Trader;
import org.springframework.stereotype.Component;

@Component
public class TraderRestMapper {
    TraderResponse mapModelToResponse(Trader trader) {
        return new TraderResponse(trader.getId(), trader.getUserId());
    }
}