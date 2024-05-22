package com.flacko.balance.webapp.rest;

import com.auth0.jwt.JWT;
import com.flacko.balance.service.*;
import com.flacko.common.exception.*;
import com.flacko.security.SecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/trader-teams/{traderTeamId}/deposit")
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

    @PutMapping("/trader-teams/{traderTeamId}/withdraw")
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

    @PutMapping("/merchants/{merchantId}/incoming/deposit")
    public BalanceResponse depositMerchantIncomingBalance(@PathVariable String merchantId,
                                                          @RequestBody BalanceUpdateRequest balanceUpdateRequest)
            throws BalanceNotFoundException, TraderTeamNotFoundException, MerchantNotFoundException,
            BalanceMissingRequiredAttributeException, UserNotFoundException, BalanceInvalidCurrentBalanceException,
            MerchantInvalidFeeRateException, MerchantMissingRequiredAttributeException {
        BalanceBuilder builder = balanceService.update(merchantId, EntityType.MERCHANT, BalanceType.INCOMING)
                .deposit(balanceUpdateRequest.amount());
        Balance balance = builder.build();
        return balanceRestMapper.mapModelToResponse(balance);
    }

    @PutMapping("/merchants/{merchantId}/incoming/withdraw")
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

    @PutMapping("/merchants/{merchantId}/outgoing/deposit")
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

    @PutMapping("/merchants/{merchantId}/outgoing/withdraw")
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

}
