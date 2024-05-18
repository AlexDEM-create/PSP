package impl;

import service.Stats;
import service.StatsBuilder;

public interface InitializableStatsBuilder extends StatsBuilder {

    StatsBuilder initializeNew();

    StatsBuilder initializeExisting(Stats existingStats);
}