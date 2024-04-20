package com.flacko.terminal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TerminalRepository extends CrudRepository<TerminalPojo, Long> {

    @Query("SELECT t FROM terminals t WHERE t.id = ?1")
    Optional<Terminal> findById(String id);

    @Query("SELECT t FROM terminals t WHERE t.traderId = ?1")
    List<Terminal> listByTraderId(String traderId);

}
