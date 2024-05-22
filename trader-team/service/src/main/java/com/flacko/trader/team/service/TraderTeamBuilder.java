package com.flacko.trader.team.service;

import com.flacko.common.country.Country;
import com.flacko.common.exception.*;
import com.flacko.trader.team.service.exception.TraderTeamIllegalLeaderException;
import com.flacko.trader.team.service.exception.TraderTeamInvalidFeeRateException;
import com.flacko.trader.team.service.exception.TraderTeamMissingRequiredAttributeException;

import java.math.BigDecimal;

public interface TraderTeamBuilder {

    TraderTeamBuilder withName(String name);

    TraderTeamBuilder withUserId(String userId);

    TraderTeamBuilder withCountry(Country country);

    TraderTeamBuilder withLeaderId(String leaderId);

    TraderTeamBuilder withTraderIncomingFeeRate(BigDecimal traderIncomingFeeRate);

    TraderTeamBuilder withTraderOutgoingFeeRate(BigDecimal traderOutgoingFeeRate);

    TraderTeamBuilder withLeaderIncomingFeeRate(BigDecimal leaderIncomingFeeRate);

    TraderTeamBuilder withLeaderOutgoingFeeRate(BigDecimal leaderOutgoingFeeRate);

    TraderTeamBuilder withVerified();

    TraderTeamBuilder withIncomingOnline(boolean incomingOnline);

    TraderTeamBuilder withOutgoingOnline(boolean outgoingOnline);

    TraderTeamBuilder withKickedOut(boolean kickedOut);

    TraderTeamBuilder withArchived();

    TraderTeam build() throws TraderTeamMissingRequiredAttributeException, UserNotFoundException,
            TraderTeamIllegalLeaderException, TraderTeamInvalidFeeRateException, TraderTeamNotFoundException,
            MerchantNotFoundException, BalanceMissingRequiredAttributeException, TraderTeamNotAllowedOnlineException,
            BalanceInvalidCurrentBalanceException, MerchantInvalidFeeRateException,
            MerchantMissingRequiredAttributeException, OutgoingPaymentIllegalStateTransitionException, UnauthorizedAccessException, OutgoingPaymentMissingRequiredAttributeException, PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException, OutgoingPaymentNotFoundException, NoEligibleTraderTeamsException, MerchantInsufficientOutgoingBalanceException;

}
