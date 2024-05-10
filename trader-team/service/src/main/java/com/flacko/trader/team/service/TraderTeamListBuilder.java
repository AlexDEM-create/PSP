package com.flacko.trader.team.service;

import com.flacko.common.country.Country;

import java.util.List;

public interface TraderTeamListBuilder {

    TraderTeamListBuilder withOnline(Boolean online);

    TraderTeamListBuilder withKickedOut(Boolean kickedOut);

    TraderTeamListBuilder withLeaderId(String leaderId);

    TraderTeamListBuilder withCountry(Country country);

    List<TraderTeam> build();

}
