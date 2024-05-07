package com.flacko.trader.team.service;

import java.util.List;

public interface TraderTeamListBuilder {

    TraderTeamListBuilder withOnline(Boolean online);

    TraderTeamListBuilder withKickedOut(Boolean kickedOut);

    TraderTeamListBuilder withLeaderId(String leaderId);

    List<TraderTeam> build();

}
