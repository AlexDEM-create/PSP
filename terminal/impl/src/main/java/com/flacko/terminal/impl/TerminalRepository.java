package com.flacko.terminal.impl;

import com.flacko.terminal.service.Terminal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TerminalRepository extends CrudRepository<TerminalPojo, Long> {

    @Query("SELECT t FROM TerminalPojo t WHERE t.id = :id")
    Optional<Terminal> findById(String id);

    @Query("SELECT t FROM TerminalPojo t WHERE t.traderTeamId = :traderTeamId")
    List<Terminal> listByTraderTeamId(String traderTeamId);

}
