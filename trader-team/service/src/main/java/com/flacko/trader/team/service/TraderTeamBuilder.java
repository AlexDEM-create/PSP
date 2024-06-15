package com.flacko.trader.team.service;

import com.flacko.common.country.Country;
import com.flacko.common.exception.BalanceInvalidCurrentBalanceException;
import com.flacko.common.exception.BalanceMissingRequiredAttributeException;
import com.flacko.common.exception.MerchantInsufficientOutgoingBalanceException;
import com.flacko.common.exception.MerchantInvalidFeeRateException;
import com.flacko.common.exception.MerchantMissingRequiredAttributeException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.NoEligibleTraderTeamsException;
import com.flacko.common.exception.OutgoingPaymentIllegalStateTransitionException;
import com.flacko.common.exception.OutgoingPaymentInvalidAmountException;
import com.flacko.common.exception.OutgoingPaymentMissingRequiredAttributeException;
import com.flacko.common.exception.OutgoingPaymentNotFoundException;
import com.flacko.common.exception.PaymentMethodNotFoundException;
import com.flacko.common.exception.TraderTeamIllegalLeaderException;
import com.flacko.common.exception.TraderTeamInvalidFeeRateException;
import com.flacko.common.exception.TraderTeamMissingRequiredAttributeException;
import com.flacko.common.exception.TraderTeamNotAllowedOnlineException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.exception.UserNotFoundException;

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
            MerchantMissingRequiredAttributeException, OutgoingPaymentIllegalStateTransitionException,
            OutgoingPaymentMissingRequiredAttributeException, PaymentMethodNotFoundException,
            OutgoingPaymentInvalidAmountException, OutgoingPaymentNotFoundException, NoEligibleTraderTeamsException,
            MerchantInsufficientOutgoingBalanceException;

}
