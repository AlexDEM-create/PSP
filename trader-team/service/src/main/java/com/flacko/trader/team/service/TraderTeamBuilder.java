package com.flacko.trader.team.service;

import com.flacko.common.exception.BalanceMissingRequiredAttributeException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.exception.UserNotFoundException;
import com.flacko.trader.team.service.exception.TraderTeamIllegalLeaderException;
import com.flacko.trader.team.service.exception.TraderTeamInvalidFeeRateException;
import com.flacko.trader.team.service.exception.TraderTeamMissingRequiredAttributeException;

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

    TraderTeam build() throws TraderTeamMissingRequiredAttributeException, UserNotFoundException,
            TraderTeamIllegalLeaderException, TraderTeamInvalidFeeRateException, TraderTeamNotFoundException,
            MerchantNotFoundException, BalanceMissingRequiredAttributeException;

}
