package com.flacko.trader.TraderTeam;

public interface InitializableTraderTeamBuilder extends TraderTeamBuilder {
    TraderTeamBuilder initializeNew();
    TraderTeamBuilder initializeExisting(TraderTeam existingTraderTeam);
}
