package com.flacko.balance.webapp.rest;

import com.flacko.balance.service.Balance;
import com.flacko.balance.service.BalanceBuilder;
import com.flacko.balance.service.BalanceService;
import com.flacko.balance.service.EntityType;
import com.flacko.common.exception.BalanceMissingRequiredAttributeException;
import com.flacko.common.exception.BalanceNotFoundException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
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
