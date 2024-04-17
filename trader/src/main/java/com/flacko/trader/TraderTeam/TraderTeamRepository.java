package com.flacko.trader.TraderTeam;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraderTeamRepository extends CrudRepository<TraderTeamPojo, Long> {
}