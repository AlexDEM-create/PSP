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
                traderTeam.getTraderIncomingFeeRate(),
                traderTeam.getTraderOutgoingFeeRate(),
                traderTeam.getLeaderIncomingFeeRate(),
                traderTeam.getLeaderOutgoingFeeRate(),
                traderTeam.isVerified(),
                traderTeam.isIncomingOnline(),
                traderTeam.isOutgoingOnline(),
                traderTeam.isKickedOut(),
                traderTeam.getCreatedDate().atZone(ZoneId.systemDefault()),
                traderTeam.getUpdatedDate().atZone(ZoneId.systemDefault()));
    }

}
