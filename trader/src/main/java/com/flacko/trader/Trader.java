package com.flacko.trader;

import com.flacko.trader.exception.TraderMissingRequiredAttributeException;

public interface Trader {
    String getId();
    String getName();
    String getUserId();
    String getTraderTeamId();
}