package com.flacko.trader.team.service;

import com.flacko.common.country.Country;

import java.util.List;

public interface TraderTeamListBuilder {

    TraderTeamListBuilder withVerified(Boolean verified);

    TraderTeamListBuilder withIncomingOnline(Boolean incomingOnline);

    TraderTeamListBuilder withOutgoingOnline(Boolean outgoingOnline);

    TraderTeamListBuilder withKickedOut(Boolean kickedOut);

    TraderTeamListBuilder withLeaderId(String leaderId);

    TraderTeamListBuilder withCountry(Country country);

    List<TraderTeam> build();

}
