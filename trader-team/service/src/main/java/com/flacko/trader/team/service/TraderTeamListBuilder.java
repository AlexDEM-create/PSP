package com.flacko.trader.team.service;

import java.util.List;

public interface TraderTeamListBuilder {

    TraderTeamListBuilder withKickedOut(Boolean kickedOut);

    TraderTeamListBuilder withLeaderId(String leaderId);

    List<TraderTeam> build();

}
