package com.flacko.trader.team.webapp.rest;

import com.flacko.common.exception.BalanceMissingRequiredAttributeException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.exception.UserNotFoundException;
import com.flacko.trader.team.service.TraderTeam;
import com.flacko.trader.team.service.TraderTeamBuilder;
import com.flacko.trader.team.service.TraderTeamListBuilder;
import com.flacko.trader.team.service.TraderTeamService;
import com.flacko.trader.team.service.exception.TraderTeamIllegalLeaderException;
import com.flacko.trader.team.service.exception.TraderTeamInvalidFeeRateException;
import com.flacko.trader.team.service.exception.TraderTeamMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trader-teams")
public class TraderTeamController {
    private final TraderTeamService traderTeamService;
    private final TraderTeamRestMapper traderTeamRestMapper;

    @GetMapping
    public List<TraderTeamResponse> list(TraderTeamFilterRequest traderTeamFilterRequest) {
        TraderTeamListBuilder builder = traderTeamService.list();
        traderTeamFilterRequest.kickedOut().ifPresent(builder::withKickedOut);
        traderTeamFilterRequest.leaderId().ifPresent(builder::withLeaderId);
        return builder.build()
                .stream()
                .map(traderTeamRestMapper::mapModelToResponse)
                .skip(traderTeamFilterRequest.offset())
                .limit(traderTeamFilterRequest.limit())
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
            MerchantNotFoundException, BalanceMissingRequiredAttributeException {
        TraderTeamBuilder builder = traderTeamService.create();
        builder.withName(traderTeamCreateRequest.name())
                .withUserId(traderTeamCreateRequest.userId())
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
            BalanceMissingRequiredAttributeException {
        TraderTeamBuilder builder = traderTeamService.update(traderTeamId);
        builder.withArchived();
        TraderTeam traderTeam = builder.build();
        return traderTeamRestMapper.mapModelToResponse(traderTeam);
    }

    @PatchMapping("/{traderTeamId}/online")
    public TraderTeamResponse online(@PathVariable String traderTeamId)
            throws TraderTeamNotFoundException, TraderTeamMissingRequiredAttributeException, UserNotFoundException,
            TraderTeamIllegalLeaderException, TraderTeamInvalidFeeRateException, MerchantNotFoundException,
            BalanceMissingRequiredAttributeException {
        TraderTeamBuilder builder = traderTeamService.update(traderTeamId);
        builder.withOnline(true);
        TraderTeam traderTeam = builder.build();
        return traderTeamRestMapper.mapModelToResponse(traderTeam);
    }

    @PatchMapping("/{traderTeamId}/kick-out")
    public TraderTeamResponse kickOut(@PathVariable String traderTeamId)
            throws TraderTeamNotFoundException, TraderTeamMissingRequiredAttributeException, UserNotFoundException,
            TraderTeamIllegalLeaderException, TraderTeamInvalidFeeRateException, MerchantNotFoundException,
            BalanceMissingRequiredAttributeException {
        TraderTeamBuilder builder = traderTeamService.update(traderTeamId);
        builder.withKickedOut(true);
        TraderTeam traderTeam = builder.build();
        return traderTeamRestMapper.mapModelToResponse(traderTeam);
    }

}
