package com.flacko.trader.team;

import com.flacko.trader.team.exception.TraderTeamMissingRequiredAttributeException;

import java.math.BigDecimal;

public interface TraderTeamBuilder {

    TraderTeamBuilder withName(String name);

    TraderTeamBuilder withUserId(String userId);

    TraderTeamBuilder withLeaderId(String leaderId);

    TraderTeamBuilder withTraderIncomingFeeRate(BigDecimal traderIncomingFeeRate);

    TraderTeamBuilder withTraderOutgoingFeeRate(BigDecimal traderOutgoingFeeRate);

    TraderTeamBuilder withLeaderIncomingFeeRate(BigDecimal leaderIncomingFeeRate);

    TraderTeamBuilder withLeaderOutgoingFeeRate(BigDecimal leaderOutgoingFeeRate);

    TraderTeamBuilder withKickedOut(boolean kickedOut);

    TraderTeamBuilder withArchived();

    TraderTeam build() throws TraderTeamMissingRequiredAttributeException;

}