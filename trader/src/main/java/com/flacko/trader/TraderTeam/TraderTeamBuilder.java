package com.flacko.trader.TraderTeam;

import com.flacko.trader.TraderBuilder;
import com.flacko.trader.TraderTeam.exception.TraderTeamMissingRequiredAttributeException;

public interface TraderTeamBuilder {
    TraderTeamBuilder withId(String id);
    TraderTeamBuilder withName(String name);
    TraderTeamBuilder withIsKickedOut(Boolean isKickedOut);
    TraderTeamBuilder withArchived();

    TraderTeam build() throws TraderTeamMissingRequiredAttributeException;

}