package com.flacko.trader.team;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TraderTeamRepository extends CrudRepository<TraderTeamPojo, Long> {

    @Query("SELECT tt FROM TraderTeamPojo tt WHERE tt.id = :id")
    Optional<TraderTeam> findById(String id);

    @Query("SELECT tt FROM TraderTeamPojo tt WHERE tt.leaderId = :leaderId")
    List<TraderTeam> listByLeaderId(String leaderId);

}