package com.flacko.trader;

public interface InitializableTraderBuilder extends TraderBuilder {
    TraderBuilder initializeNew();
    TraderBuilder initializeExisting(Trader existingTrader);

    TraderBuilder withTraderTeamId(String traderTeamId);
}
