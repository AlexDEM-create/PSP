package com.flacko.trader.team.service;

import com.flacko.common.country.Country;

import java.util.List;

public interface TraderTeamListBuilder {

    TraderTeamListBuilder withUserId(String userId);

    TraderTeamListBuilder withVerified(Boolean verified);

    TraderTeamListBuilder withIncomingOnline(Boolean incomingOnline);

    TraderTeamListBuilder withOutgoingOnline(Boolean outgoingOnline);

    TraderTeamListBuilder withKickedOut(Boolean kickedOut);

    TraderTeamListBuilder withLeaderId(String leaderId);

    TraderTeamListBuilder withCountry(Country country);

    TraderTeamListBuilder withArchived(Boolean archived);

    List<TraderTeam> build();

}
