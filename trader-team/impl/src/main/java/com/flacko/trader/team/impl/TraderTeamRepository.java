package com.flacko.trader.team.impl;

import com.flacko.trader.team.service.TraderTeam;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TraderTeamRepository extends CrudRepository<TraderTeamPojo, Long>,
        JpaSpecificationExecutor<TraderTeam> {

    @Query("SELECT tt FROM TraderTeamPojo tt WHERE tt.id = :id")
    Optional<TraderTeam> findById(String id);

    @Query("SELECT tt FROM TraderTeamPojo tt WHERE tt.userId = :userId")
    Optional<TraderTeam> findByUserId(String userId);

}
