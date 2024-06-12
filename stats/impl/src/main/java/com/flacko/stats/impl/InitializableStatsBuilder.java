package com.flacko.stats.impl;

import com.flacko.stats.service.Stats;
import com.flacko.stats.service.StatsBuilder;

public interface InitializableStatsBuilder extends StatsBuilder {

    StatsBuilder initializeNew();

    StatsBuilder initializeExisting(Stats existingStats);

}
