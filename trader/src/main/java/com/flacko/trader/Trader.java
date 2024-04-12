package com.flacko.trader;

import com.flacko.trader.exception.TraderMissingRequiredAttributeException;

public interface Trader {
    String getId();
    String getName();
    String getUserId();
    String getTradersTeam();

    TraderBuilder withId(String id);
    TraderBuilder withName(String name);
    Trader build() throws TraderMissingRequiredAttributeException;

    void setName(String name);
}