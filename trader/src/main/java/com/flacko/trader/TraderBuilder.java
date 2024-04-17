package com.flacko.trader;

import com.flacko.trader.exception.TraderMissingRequiredAttributeException;

public interface TraderBuilder {
    TraderBuilder withId(String id);
    TraderBuilder withName(String name);
    TraderBuilder withUserId(String name);

    TraderBuilder withArchived();

    Trader build() throws TraderMissingRequiredAttributeException;
}


