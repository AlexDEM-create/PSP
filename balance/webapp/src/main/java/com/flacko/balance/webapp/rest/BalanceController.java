package com.flacko.balance.webapp.rest;

import com.auth0.jwt.JWT;
import com.flacko.balance.service.Balance;
import com.flacko.balance.service.BalanceBuilder;
import com.flacko.balance.service.BalanceService;
import com.flacko.balance.service.BalanceType;
import com.flacko.balance.service.EntityType;
import com.flacko.common.exception.BalanceInvalidCurrentBalanceException;
import com.flacko.common.exception.BalanceMissingRequiredAttributeException;
import com.flacko.common.exception.BalanceNotFoundException;
import com.flacko.common.exception.MerchantInvalidFeeRateException;
import com.flacko.common.exception.MerchantMissingRequiredAttributeException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.exception.UserNotFoundException;
import com.flacko.security.SecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/balances")
public class BalanceController {

    private final BalanceService balanceService;
    private final BalanceRestMapper balanceRestMapper;

    @GetMapping("/my")
    public List<BalanceResponse> getMyBalances(@RequestHeader("Authorization") String tokenWithPrefix)
            throws BalanceNotFoundException, UserNotFoundException, TraderTeamNotFoundException,
            MerchantNotFoundException {
        String token = tokenWithPrefix.substring(SecurityConfig.TOKEN_PREFIX.length());
        String login = JWT.decode(token).getSubject();
        return balanceService.getMy(login)
                .stream()
                .map(balanceRestMapper::mapModelToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/trader-teams/{traderTeamId}")
    public BalanceResponse getTraderTeamBalance(@PathVariable String traderTeamId) throws BalanceNotFoundException {
        return balanceRestMapper.mapModelToResponse(balanceService.get(traderTeamId, EntityType.TRADER_TEAM,
                BalanceType.GENERIC));
    }

    @GetMapping("/merchants/{merchantId}/incoming")
    public BalanceResponse getMerchantIncomingBalance(@PathVariable String merchantId) throws BalanceNotFoundException {
        return balanceRestMapper.mapModelToResponse(balanceService.get(merchantId, EntityType.MERCHANT,
                BalanceType.INCOMING));
    }

    @GetMapping("/merchants/{merchantId}/outgoing")
    public BalanceResponse getMerchantOutgoingBalance(@PathVariable String merchantId) throws BalanceNotFoundException {
        return balanceRestMapper.mapModelToResponse(balanceService.get(merchantId, EntityType.MERCHANT,
                BalanceType.OUTGOING));
    }

    @PatchMapping("/trader-teams/{traderTeamId}/deposit")
    public BalanceResponse depositTraderTeamBalance(@PathVariable String traderTeamId,
                                                    @RequestBody BalanceUpdateRequest balanceUpdateRequest)
            throws BalanceNotFoundException, TraderTeamNotFoundException, MerchantNotFoundException,
            BalanceMissingRequiredAttributeException, UserNotFoundException, BalanceInvalidCurrentBalanceException,
            MerchantInvalidFeeRateException, MerchantMissingRequiredAttributeException {
        BalanceBuilder builder = balanceService.update(traderTeamId, EntityType.TRADER_TEAM, BalanceType.GENERIC)
                .deposit(balanceUpdateRequest.amount());
        Balance balance = builder.build();
        return balanceRestMapper.mapModelToResponse(balance);
    }

    @PatchMapping("/trader-teams/{traderTeamId}/withdraw")
    public BalanceResponse withdrawTraderTeamBalance(@PathVariable String traderTeamId,
                                                     @RequestBody BalanceUpdateRequest balanceUpdateRequest)
            throws BalanceNotFoundException, TraderTeamNotFoundException, MerchantNotFoundException,
            BalanceMissingRequiredAttributeException, UserNotFoundException, BalanceInvalidCurrentBalanceException,
            MerchantInvalidFeeRateException, MerchantMissingRequiredAttributeException {
        BalanceBuilder builder = balanceService.update(traderTeamId, EntityType.TRADER_TEAM, BalanceType.GENERIC)
                .withdraw(balanceUpdateRequest.amount());
        Balance balance = builder.build();
        return balanceRestMapper.mapModelToResponse(balance);
    }

    @PatchMapping("/merchants/{merchantId}/incoming/withdraw")
    public BalanceResponse withdrawMerchantIncomingBalance(@PathVariable String merchantId,
                                                           @RequestBody BalanceUpdateRequest balanceUpdateRequest)
            throws BalanceNotFoundException, TraderTeamNotFoundException, MerchantNotFoundException,
            BalanceMissingRequiredAttributeException, UserNotFoundException, BalanceInvalidCurrentBalanceException,
            MerchantInvalidFeeRateException, MerchantMissingRequiredAttributeException {
        BalanceBuilder builder = balanceService.update(merchantId, EntityType.MERCHANT, BalanceType.INCOMING)
                .withdraw(balanceUpdateRequest.amount());
        Balance balance = builder.build();
        return balanceRestMapper.mapModelToResponse(balance);
    }

    @PatchMapping("/merchants/{merchantId}/outgoing/deposit")
    public BalanceResponse depositMerchantOutgoingBalance(@PathVariable String merchantId,
                                                          @RequestBody BalanceUpdateRequest balanceUpdateRequest)
            throws BalanceNotFoundException, TraderTeamNotFoundException, MerchantNotFoundException,
            BalanceMissingRequiredAttributeException, UserNotFoundException, BalanceInvalidCurrentBalanceException,
            MerchantInvalidFeeRateException, MerchantMissingRequiredAttributeException {
        BalanceBuilder builder = balanceService.update(merchantId, EntityType.MERCHANT, BalanceType.OUTGOING)
                .deposit(balanceUpdateRequest.amount());
        Balance balance = builder.build();
        return balanceRestMapper.mapModelToResponse(balance);
    }

    @PatchMapping("/merchants/{merchantId}/outgoing/withdraw")
    public BalanceResponse withdrawMerchantOutgoingBalance(@PathVariable String merchantId,
                                                           @RequestBody BalanceUpdateRequest balanceUpdateRequest)
            throws BalanceNotFoundException, TraderTeamNotFoundException, MerchantNotFoundException,
            BalanceMissingRequiredAttributeException, UserNotFoundException, BalanceInvalidCurrentBalanceException,
            MerchantInvalidFeeRateException, MerchantMissingRequiredAttributeException {
        BalanceBuilder builder = balanceService.update(merchantId, EntityType.MERCHANT, BalanceType.OUTGOING)
                .withdraw(balanceUpdateRequest.amount());
        Balance balance = builder.build();
        return balanceRestMapper.mapModelToResponse(balance);
    }

    @PatchMapping("/merchants/{merchantId}/transfer")
    public BalanceResponse transferFromIncomingToOutgoing(@PathVariable String merchantId,
                                                          @RequestBody BalanceUpdateRequest balanceUpdateRequest)
            throws BalanceNotFoundException, UserNotFoundException, BalanceInvalidCurrentBalanceException,
            TraderTeamNotFoundException, MerchantNotFoundException, MerchantInvalidFeeRateException,
            MerchantMissingRequiredAttributeException, BalanceMissingRequiredAttributeException {
        balanceService.update(merchantId, EntityType.MERCHANT, BalanceType.INCOMING)
                .withdraw(balanceUpdateRequest.amount())
                .build();
        Balance outgoingBalance = balanceService.update(merchantId, EntityType.MERCHANT, BalanceType.OUTGOING)
                .deposit(balanceUpdateRequest.amount())
                .build();
        return balanceRestMapper.mapModelToResponse(outgoingBalance);
    }


}
