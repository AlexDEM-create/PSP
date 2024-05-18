package webapp.rest;

import org.springframework.stereotype.Component;
import service.Stats;
import webapp.rest.StatsResponse;

import java.time.ZoneId;

@Component
public class StatsRestMapper {

    StatsResponse mapModelToResponse(Stats stats) {
        return new StatsResponse(stats.getId(),
                stats.getEntityId(),
                stats.getEntityType(),
                stats.getTodayOutgoingTotal(),
                stats.getTodayIncomingTotal(),
                stats.getAllTimeOutgoingTotal(),
                stats.getAllTimeIncomingTotal(),
                stats.getCreatedDate().atZone(ZoneId.systemDefault()),
                stats.getUpdatedDate().atZone(ZoneId.systemDefault()));
    }

}

