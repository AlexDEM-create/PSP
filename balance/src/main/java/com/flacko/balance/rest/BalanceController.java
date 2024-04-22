package com.flacko.balance.rest;

import com.flacko.balance.Balance;
import com.flacko.balance.BalanceBuilder;
import com.flacko.balance.BalanceService;
import com.flacko.balance.EntityType;
import com.flacko.balance.exception.BalanceMissingRequiredAttributeException;
import com.flacko.balance.exception.BalanceNotFoundException;
import com.flacko.merchant.exception.MerchantNotFoundException;
import com.flacko.trader.team.exception.TraderTeamNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/balances")
public class BalanceController {

    private final BalanceService balanceService;
    private final BalanceRestMapper balanceRestMapper;

    @GetMapping("/trader-teams/{traderTeamId}")
    public BalanceResponse getTraderTeamBalance(@PathVariable String traderTeamId) throws BalanceNotFoundException {
        return balanceRestMapper.mapModelToResponse(balanceService.get(traderTeamId, EntityType.TRADER_TEAM));
    }

    @GetMapping("/merchants/{merchantId}")
    public BalanceResponse getMerchantBalance(@PathVariable String merchantId) throws BalanceNotFoundException {
        return balanceRestMapper.mapModelToResponse(balanceService.get(merchantId, EntityType.MERCHANT));
    }

    @PutMapping("/trader-teams/{traderTeamId}")
    public BalanceResponse updateTraderTeamBalance(@PathVariable String traderTeamId,
                                                   @RequestBody BalanceUpdateRequest balanceUpdateRequest)
            throws BalanceNotFoundException, TraderTeamNotFoundException, MerchantNotFoundException,
            BalanceMissingRequiredAttributeException {
        BalanceBuilder builder = balanceService.update(traderTeamId, EntityType.TRADER_TEAM)
                .deposit(balanceUpdateRequest.amount());
        Balance balance = builder.build();
        return balanceRestMapper.mapModelToResponse(balance);
    }

    @PutMapping("/merchants/{merchantId}")
    public BalanceResponse updateMerchantBalance(@PathVariable String merchantId,
                                                 @RequestBody BalanceUpdateRequest balanceUpdateRequest)
            throws BalanceNotFoundException, TraderTeamNotFoundException, MerchantNotFoundException,
            BalanceMissingRequiredAttributeException {
        BalanceBuilder builder = balanceService.update(merchantId, EntityType.MERCHANT)
                .deposit(balanceUpdateRequest.amount());
        Balance balance = builder.build();
        return balanceRestMapper.mapModelToResponse(balance);
    }

}
