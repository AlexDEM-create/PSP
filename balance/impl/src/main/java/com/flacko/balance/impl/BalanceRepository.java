package com.flacko.balance.impl;

import com.flacko.balance.service.Balance;
import com.flacko.balance.service.BalanceType;
import com.flacko.balance.service.EntityType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalanceRepository extends CrudRepository<BalancePojo, Long>, JpaSpecificationExecutor<Balance> {

    @Query("SELECT b FROM BalancePojo b WHERE b.entityId = :entityId AND b.entityType = :entityType AND b.type = :type")
    Optional<Balance> findByEntityIdAndEntityTypeAndType(String entityId, EntityType entityType, BalanceType type);

}
