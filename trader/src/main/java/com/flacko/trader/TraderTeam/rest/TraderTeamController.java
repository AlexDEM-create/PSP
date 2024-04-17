package com.flacko.trader.TraderTeam.rest;

import com.flacko.trader.TraderTeam.TraderTeam;
import com.flacko.trader.TraderTeam.TraderTeamBuilder;
import com.flacko.trader.TraderTeam.TraderTeamService;
import com.flacko.trader.TraderTeam.exception.TraderTeamMissingRequiredAttributeException;
import com.flacko.trader.TraderTeam.exception.TraderTeamNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/traderTeams")
public class TraderTeamController {
    private final TraderTeamService traderTeamService;
    private final TraderTeamRestMapper traderTeamRestMapper;

    @GetMapping
    public List<TraderTeamResponse> list() {
        return traderTeamService.list()
                .stream()
                .map(traderTeam -> {
                    try {
                        return traderTeam.build();
                    } catch (TraderTeamMissingRequiredAttributeException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(traderTeamRestMapper::mapModelToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{traderTeamId}")
    public TraderTeamResponse get(@PathVariable String traderTeamId) throws TraderTeamNotFoundException {
        return traderTeamRestMapper.mapModelToResponse((TraderTeam) traderTeamService.get(traderTeamId));
    }


    @DeleteMapping("/{traderTeamId}")
    public TraderTeamResponse archive(@PathVariable String traderTeamId)
            throws TraderTeamNotFoundException, TraderTeamMissingRequiredAttributeException {
        TraderTeamBuilder builder = traderTeamService.update(traderTeamId);
        builder.withArchived();
        TraderTeam traderTeam = builder.build();
        return traderTeamRestMapper.mapModelToResponse(traderTeam);
    }
}
