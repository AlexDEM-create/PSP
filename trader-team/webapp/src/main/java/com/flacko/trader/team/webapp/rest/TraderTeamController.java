package com.flacko.trader.team.webapp.rest;

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
import com.flacko.common.exception.TraderTeamNotAllowedOnlineException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.exception.UnauthorizedAccessException;
import com.flacko.common.exception.UserNotFoundException;
import com.flacko.trader.team.service.TraderTeam;
import com.flacko.trader.team.service.TraderTeamBuilder;
import com.flacko.trader.team.service.TraderTeamListBuilder;
import com.flacko.trader.team.service.TraderTeamService;
import com.flacko.trader.team.service.exception.TraderTeamIllegalLeaderException;
import com.flacko.trader.team.service.exception.TraderTeamInvalidFeeRateException;
import com.flacko.trader.team.service.exception.TraderTeamMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trader-teams")
public class TraderTeamController {

    private static final String VERIFIED = "verified";
    private static final String INCOMING_ONLINE = "incoming_online";
    private static final String OUTGOING_ONLINE = "outgoing_online";
    private static final String KICKED_OUT = "kicked_out";
    private static final String LEADER_ID = "leader_id";
    private static final String COUNTRY = "country";
    private static final String ARCHIVED = "archived";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

    private final TraderTeamService traderTeamService;
    private final TraderTeamRestMapper traderTeamRestMapper;

    @GetMapping
    public List<TraderTeamResponse> list(@RequestParam(VERIFIED) Optional<Boolean> verified,
                                         @RequestParam(INCOMING_ONLINE) Optional<Boolean> incomingOnline,
                                         @RequestParam(OUTGOING_ONLINE) Optional<Boolean> outgoingOnline,
                                         @RequestParam(KICKED_OUT) Optional<Boolean> kickedOut,
                                         @RequestParam(LEADER_ID) Optional<String> leaderId,
                                         @RequestParam(COUNTRY) Optional<Country> country,
                                         @RequestParam(ARCHIVED) Optional<Boolean> archived,
                                         @RequestParam(value = LIMIT, defaultValue = "10") Integer limit,
                                         @RequestParam(value = OFFSET, defaultValue = "0") Integer offset) {
        TraderTeamListBuilder builder = traderTeamService.list();
        verified.ifPresent(builder::withVerified);
        incomingOnline.ifPresent(builder::withIncomingOnline);
        outgoingOnline.ifPresent(builder::withOutgoingOnline);
        kickedOut.ifPresent(builder::withKickedOut);
        leaderId.ifPresent(builder::withLeaderId);
        country.ifPresent(builder::withCountry);
        archived.ifPresent(builder::withArchived);
        return builder.build()
                .stream()
                .map(traderTeamRestMapper::mapModelToResponse)
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @GetMapping("/{traderTeamId}")
    public TraderTeamResponse get(@PathVariable String traderTeamId) throws TraderTeamNotFoundException {
        return traderTeamRestMapper.mapModelToResponse(traderTeamService.get(traderTeamId));
    }

    @PostMapping
    public TraderTeamResponse create(@RequestBody TraderTeamCreateRequest traderTeamCreateRequest)
            throws TraderTeamMissingRequiredAttributeException, UserNotFoundException,
            TraderTeamIllegalLeaderException, TraderTeamInvalidFeeRateException, TraderTeamNotFoundException,
            MerchantNotFoundException, BalanceMissingRequiredAttributeException, TraderTeamNotAllowedOnlineException,
            BalanceInvalidCurrentBalanceException, MerchantInvalidFeeRateException,
            MerchantMissingRequiredAttributeException, OutgoingPaymentIllegalStateTransitionException,
            UnauthorizedAccessException, OutgoingPaymentMissingRequiredAttributeException,
            PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException, OutgoingPaymentNotFoundException,
            NoEligibleTraderTeamsException, MerchantInsufficientOutgoingBalanceException {
        TraderTeamBuilder builder = traderTeamService.create();
        builder.withName(traderTeamCreateRequest.name())
                .withUserId(traderTeamCreateRequest.userId())
                .withCountry(traderTeamCreateRequest.country())
                .withLeaderId(traderTeamCreateRequest.leaderId())
                .withTraderIncomingFeeRate(traderTeamCreateRequest.traderIncomingFeeRate())
                .withTraderOutgoingFeeRate(traderTeamCreateRequest.traderOutgoingFeeRate())
                .withLeaderIncomingFeeRate(traderTeamCreateRequest.leaderIncomingFeeRate())
                .withLeaderOutgoingFeeRate(traderTeamCreateRequest.leaderOutgoingFeeRate());
        TraderTeam traderTeam = builder.build();
        return traderTeamRestMapper.mapModelToResponse(traderTeam);
    }

    @DeleteMapping("/{traderTeamId}")
    public TraderTeamResponse archive(@PathVariable String traderTeamId)
            throws TraderTeamNotFoundException, TraderTeamMissingRequiredAttributeException, UserNotFoundException,
            TraderTeamIllegalLeaderException, TraderTeamInvalidFeeRateException, MerchantNotFoundException,
            BalanceMissingRequiredAttributeException, TraderTeamNotAllowedOnlineException,
            BalanceInvalidCurrentBalanceException, MerchantInvalidFeeRateException,
            MerchantMissingRequiredAttributeException, OutgoingPaymentIllegalStateTransitionException,
            UnauthorizedAccessException, OutgoingPaymentMissingRequiredAttributeException,
            PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException, OutgoingPaymentNotFoundException,
            NoEligibleTraderTeamsException, MerchantInsufficientOutgoingBalanceException {
        TraderTeamBuilder builder = traderTeamService.update(traderTeamId);
        builder.withArchived();
        TraderTeam traderTeam = builder.build();
        return traderTeamRestMapper.mapModelToResponse(traderTeam);
    }

    @PatchMapping("/{traderTeamId}/incoming-online")
    public TraderTeamResponse incomingOnline(@PathVariable String traderTeamId)
            throws TraderTeamNotFoundException, TraderTeamMissingRequiredAttributeException, UserNotFoundException,
            TraderTeamIllegalLeaderException, TraderTeamInvalidFeeRateException, MerchantNotFoundException,
            BalanceMissingRequiredAttributeException, TraderTeamNotAllowedOnlineException,
            BalanceInvalidCurrentBalanceException, MerchantInvalidFeeRateException,
            MerchantMissingRequiredAttributeException, OutgoingPaymentIllegalStateTransitionException,
            UnauthorizedAccessException, OutgoingPaymentMissingRequiredAttributeException,
            PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException, OutgoingPaymentNotFoundException,
            NoEligibleTraderTeamsException, MerchantInsufficientOutgoingBalanceException {
        TraderTeamBuilder builder = traderTeamService.update(traderTeamId);
        builder.withIncomingOnline(true);
        TraderTeam traderTeam = builder.build();
        return traderTeamRestMapper.mapModelToResponse(traderTeam);
    }

    @PatchMapping("/{traderTeamId}/incoming-offline")
    public TraderTeamResponse incomingOffline(@PathVariable String traderTeamId)
            throws TraderTeamNotFoundException, TraderTeamMissingRequiredAttributeException, UserNotFoundException,
            TraderTeamIllegalLeaderException, TraderTeamInvalidFeeRateException, MerchantNotFoundException,
            BalanceMissingRequiredAttributeException, TraderTeamNotAllowedOnlineException,
            BalanceInvalidCurrentBalanceException, MerchantInvalidFeeRateException,
            MerchantMissingRequiredAttributeException, OutgoingPaymentIllegalStateTransitionException,
            UnauthorizedAccessException, OutgoingPaymentMissingRequiredAttributeException,
            PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException, OutgoingPaymentNotFoundException,
            NoEligibleTraderTeamsException, MerchantInsufficientOutgoingBalanceException {
        TraderTeamBuilder builder = traderTeamService.update(traderTeamId);
        builder.withIncomingOnline(false);
        TraderTeam traderTeam = builder.build();
        return traderTeamRestMapper.mapModelToResponse(traderTeam);
    }

    @PatchMapping("/{traderTeamId}/outgoing-online")
    public TraderTeamResponse outgoingOnline(@PathVariable String traderTeamId)
            throws TraderTeamNotFoundException, TraderTeamMissingRequiredAttributeException, UserNotFoundException,
            TraderTeamIllegalLeaderException, TraderTeamInvalidFeeRateException, MerchantNotFoundException,
            BalanceMissingRequiredAttributeException, TraderTeamNotAllowedOnlineException,
            BalanceInvalidCurrentBalanceException, MerchantInvalidFeeRateException,
            MerchantMissingRequiredAttributeException, OutgoingPaymentIllegalStateTransitionException,
            UnauthorizedAccessException, OutgoingPaymentMissingRequiredAttributeException,
            PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException, OutgoingPaymentNotFoundException,
            NoEligibleTraderTeamsException, MerchantInsufficientOutgoingBalanceException {
        TraderTeamBuilder builder = traderTeamService.update(traderTeamId);
        builder.withOutgoingOnline(true);
        TraderTeam traderTeam = builder.build();
        return traderTeamRestMapper.mapModelToResponse(traderTeam);
    }

    @PatchMapping("/{traderTeamId}/outgoing-offline")
    public TraderTeamResponse outgoingOffline(@PathVariable String traderTeamId)
            throws TraderTeamNotFoundException, TraderTeamMissingRequiredAttributeException, UserNotFoundException,
            TraderTeamIllegalLeaderException, TraderTeamInvalidFeeRateException, MerchantNotFoundException,
            BalanceMissingRequiredAttributeException, TraderTeamNotAllowedOnlineException,
            BalanceInvalidCurrentBalanceException, MerchantInvalidFeeRateException,
            MerchantMissingRequiredAttributeException, OutgoingPaymentIllegalStateTransitionException,
            UnauthorizedAccessException, OutgoingPaymentMissingRequiredAttributeException,
            PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException, OutgoingPaymentNotFoundException,
            NoEligibleTraderTeamsException, MerchantInsufficientOutgoingBalanceException {
        TraderTeamBuilder builder = traderTeamService.update(traderTeamId);
        builder.withOutgoingOnline(false);
        TraderTeam traderTeam = builder.build();
        return traderTeamRestMapper.mapModelToResponse(traderTeam);
    }

    @PatchMapping("/{traderTeamId}/kick-out")
    public TraderTeamResponse kickOut(@PathVariable String traderTeamId)
            throws TraderTeamNotFoundException, TraderTeamMissingRequiredAttributeException, UserNotFoundException,
            TraderTeamIllegalLeaderException, TraderTeamInvalidFeeRateException, MerchantNotFoundException,
            BalanceMissingRequiredAttributeException, TraderTeamNotAllowedOnlineException,
            BalanceInvalidCurrentBalanceException, MerchantInvalidFeeRateException,
            MerchantMissingRequiredAttributeException, OutgoingPaymentIllegalStateTransitionException,
            UnauthorizedAccessException, OutgoingPaymentMissingRequiredAttributeException,
            PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException, OutgoingPaymentNotFoundException,
            NoEligibleTraderTeamsException, MerchantInsufficientOutgoingBalanceException {
        TraderTeamBuilder builder = traderTeamService.update(traderTeamId);
        builder.withKickedOut(true);
        TraderTeam traderTeam = builder.build();
        return traderTeamRestMapper.mapModelToResponse(traderTeam);
    }

    @PatchMapping("/{traderTeamId}/get-back")
    public TraderTeamResponse getBack(@PathVariable String traderTeamId)
            throws TraderTeamNotFoundException, TraderTeamMissingRequiredAttributeException, UserNotFoundException,
            TraderTeamIllegalLeaderException, TraderTeamInvalidFeeRateException, MerchantNotFoundException,
            BalanceMissingRequiredAttributeException, TraderTeamNotAllowedOnlineException,
            BalanceInvalidCurrentBalanceException, MerchantInvalidFeeRateException,
            MerchantMissingRequiredAttributeException, OutgoingPaymentIllegalStateTransitionException,
            UnauthorizedAccessException, OutgoingPaymentMissingRequiredAttributeException,
            PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException, OutgoingPaymentNotFoundException,
            NoEligibleTraderTeamsException, MerchantInsufficientOutgoingBalanceException {
        TraderTeamBuilder builder = traderTeamService.update(traderTeamId);
        builder.withKickedOut(false);
        TraderTeam traderTeam = builder.build();
        return traderTeamRestMapper.mapModelToResponse(traderTeam);
    }

}
