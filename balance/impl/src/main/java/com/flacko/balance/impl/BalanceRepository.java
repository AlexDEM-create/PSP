package com.flacko.balance.impl;

import com.flacko.balance.service.Balance;
import com.flacko.balance.service.EntityType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalanceRepository extends CrudRepository<BalancePojo, Long> {

    @Query("SELECT b FROM BalancePojo b WHERE b.entityId = :entityId AND b.entityType = :entityType")
    Optional<Balance> findByEntityIdAndEntityType(String entityId, EntityType entityType);

}
