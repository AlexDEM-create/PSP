package com.flacko.reporting.impl;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.flacko.reporting.service.EntityType;
import com.flacko.reporting.service.Stats;

import java.util.Optional;

@Repository
public interface StatsRepository extends CrudRepository<StatsPojo, Long>, JpaSpecificationExecutor<Stats> {

    @Query("SELECT s FROM StatsPojo s WHERE s.entityId = :entityId AND s.entityType = :entityType")
    Optional<Stats> findByEntityIdAndEntityType(String entityId, EntityType entityType);

}
