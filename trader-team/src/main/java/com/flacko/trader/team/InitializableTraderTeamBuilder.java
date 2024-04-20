package com.flacko.trader.team;

public interface InitializableTraderTeamBuilder extends TraderTeamBuilder {

    TraderTeamBuilder initializeNew();

    TraderTeamBuilder initializeExisting(TraderTeam existingTraderTeam);

}
