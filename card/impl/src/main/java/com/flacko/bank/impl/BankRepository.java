package com.flacko.bank.impl;

import com.flacko.bank.service.Bank;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends CrudRepository<BankPojo, Long> {

    @Query("SELECT b FROM BankPojo b WHERE b.id = :id")
    Optional<Bank> findById(String id);

    @Query("SELECT b FROM BankPojo b WHERE b.name = :name")
    Optional<Bank> findByName(String name);

}
