package com.flacko.reporting.impl;

import com.flacko.reporting.service.Stats;
import com.flacko.reporting.service.StatsBuilder;

public interface InitializableStatsBuilder extends StatsBuilder {

    StatsBuilder initializeNew();

    StatsBuilder initializeExisting(Stats existingStats);

}
