package com.flacko.bank;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends CrudRepository<BankPojo, Long> {

    @Query("SELECT b FROM banks b WHERE b.id = ?1")
    Optional<Bank> findById(String id);

    @Query("SELECT b FROM banks b WHERE b.name = ?1")
    Optional<Bank> findByName(String name);

}
