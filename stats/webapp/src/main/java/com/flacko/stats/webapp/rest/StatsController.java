package com.flacko.stats.webapp.rest;

import com.flacko.stats.service.EntityType;
import com.flacko.stats.service.StatsService;
import com.flacko.stats.service.exception.StatsNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stats")
public class StatsController {

    private final StatsService statsService;
    private final StatsRestMapper statsRestMapper;

    @GetMapping("/trader-teams/{traderTeamId}")
    public StatsResponse getTraderTeamStats(@PathVariable String traderTeamId) throws StatsNotFoundException {
        return statsRestMapper.mapModelToResponse(statsService.get(traderTeamId, EntityType.TRADER_TEAM));
    }

    @GetMapping("/merchants/{merchantId}")
    public StatsResponse getMerchantStats(@PathVariable String merchantId) throws StatsNotFoundException {
        return statsRestMapper.mapModelToResponse(statsService.get(merchantId, EntityType.MERCHANT));
    }

}
