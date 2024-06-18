package com.flacko.stats.impl;

import com.flacko.common.spring.ServiceLocator;
import com.flacko.stats.service.EntityType;
import com.flacko.stats.service.Stats;
import com.flacko.stats.service.StatsBuilder;
import com.flacko.stats.service.StatsService;
import com.flacko.stats.service.exception.StatsNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    private final ServiceLocator serviceLocator;


    @Override
    public Stats get(String entityId, EntityType entityType) throws StatsNotFoundException {
        return statsRepository.findByEntityIdAndEntityType(entityId, entityType)
                .orElseThrow(() -> new StatsNotFoundException(entityId));
    }

    @Transactional
    @Override
    public StatsBuilder create() {
        return serviceLocator.create(InitializableStatsBuilder.class)
                .initializeNew();
    }

    @Transactional
    @Override
    public StatsBuilder update(String entityId, EntityType entityType) throws StatsNotFoundException {
        Stats existingStats = get(entityId, entityType);
        return serviceLocator.create(InitializableStatsBuilder.class)
                .initializeExisting(existingStats);
    }

}
