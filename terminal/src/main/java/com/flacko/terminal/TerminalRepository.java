package com.flacko.terminal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TerminalRepository extends CrudRepository<TerminalPojo, Long> {

    Optional<Terminal> findById(String id);

    List<Terminal> listByTraderId(String traderId);

}
