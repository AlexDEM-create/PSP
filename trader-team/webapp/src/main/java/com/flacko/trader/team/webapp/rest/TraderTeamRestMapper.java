package com.flacko.trader.team.webapp.rest;

import com.flacko.trader.team.service.TraderTeam;
import org.springframework.stereotype.Component;

import java.time.ZoneId;


@Component
public class TraderTeamRestMapper {

    public TraderTeamResponse mapModelToResponse(TraderTeam traderTeam) {
        return new TraderTeamResponse(
                traderTeam.getId(),
                traderTeam.getName(),
                traderTeam.getUserId(),
                traderTeam.getCountry(),
                traderTeam.getLeaderId(),
                traderTeam.getTraderIncomingFeeRate().movePointRight(2),
                traderTeam.getTraderOutgoingFeeRate().movePointRight(2),
                traderTeam.getLeaderIncomingFeeRate().movePointRight(2),
                traderTeam.getLeaderOutgoingFeeRate().movePointRight(2),
                traderTeam.isVerified(),
                traderTeam.isIncomingOnline(),
                traderTeam.isOutgoingOnline(),
                traderTeam.isKickedOut(),
                traderTeam.getCreatedDate().atZone(ZoneId.systemDefault()),
                traderTeam.getUpdatedDate().atZone(ZoneId.systemDefault()));
    }

}
