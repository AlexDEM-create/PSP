package com.flacko.trader.TraderTeam.rest;
import com.flacko.trader.TraderTeam.TraderTeam;
import org.springframework.stereotype.Component;


@Component
public class TraderTeamRestMapper {
    public TraderTeamResponse mapModelToResponse(TraderTeam traderTeam) {
        return new TraderTeamResponse(
                traderTeam.getId(),
                traderTeam.getName(),
                traderTeam.getIsKickedOut()
        );
    }
}


