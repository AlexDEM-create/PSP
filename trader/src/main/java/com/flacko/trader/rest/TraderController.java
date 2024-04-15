package com.flacko.trader.rest;

import com.flacko.trader.Trader;
import com.flacko.trader.TraderBuilder;
import com.flacko.trader.TraderService;
import com.flacko.trader.exception.TraderMissingRequiredAttributeException;
import com.flacko.trader.exception.TraderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/traders")
public class TraderController {
    private final TraderService traderService;
    private final TraderRestMapper traderRestMapper;

    @GetMapping
    public List<TraderResponse> list() {
        return traderService.list()
                .stream()
                .map(traderBuilder -> {
                    try {
                        return traderBuilder.build();
                    } catch (TraderMissingRequiredAttributeException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(traderRestMapper::mapModelToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{traderId}")
    public TraderResponse get(@PathVariable String traderId) throws TraderNotFoundException {
        return traderRestMapper.mapModelToResponse((Trader) traderService.get(traderId));
    }

}
