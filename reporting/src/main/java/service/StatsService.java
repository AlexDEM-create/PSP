package service;

import service.exception.StatsNotFoundException;

public interface StatsService {

    StatsBuilder create();

    StatsBuilder update(String entityId, EntityType entityType) throws StatsNotFoundException;

    Stats get(String entityId, EntityType entityType) throws StatsNotFoundException;

}