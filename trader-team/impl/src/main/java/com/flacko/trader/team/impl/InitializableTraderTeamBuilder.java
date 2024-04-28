package com.flacko.trader.team.impl;

import com.flacko.trader.team.service.TraderTeam;
import com.flacko.trader.team.service.TraderTeamBuilder;

public interface InitializableTraderTeamBuilder extends TraderTeamBuilder {

    TraderTeamBuilder initializeNew();

    TraderTeamBuilder initializeExisting(TraderTeam existingTraderTeam);

}
